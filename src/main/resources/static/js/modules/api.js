import { errorSwal, errorSwalPag, iniciarLazyLoading, successSwalTimWeb } from "./ui.js";
import { getFavoritosUri, getRecipeUri, getUserUri, getIngredienteUri, getPasoUri, getValoracionUri } from "./uriBuilder.js";
import { generarEstrellas, getFlagFileName, getRecipe } from "./recipe.js";
import { PATH_IMG_ICON, PATH_IMG_RECETAS } from "./const.js";
import { getUser, setUserOut, userIsLogged } from "./user.js";


export function apiLogin(usuario, password) {
	const datos = {
		usuario: usuario.value,
		contra: password.value,
	};

	fetch(getUserUri("verifyUser", usuario.value, password.value), {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(datos),
	})
		.then(async (response) => {
			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "No existe el Usuario");
			}
			return response.json();
		})
		.then(async (data) => {
			sessionStorage.setItem("usuario", JSON.stringify(data));
			successSwalTimWeb('Bienvenido a Facefood', 'Puedes empezar a guardar tus recetas 🍳', `/facefood/${usuario.value}`)
		})
		.catch((error) => {
			console.error("Error: ", error);
			errorSwalPag("Error", error.message, `/facefood/login`);
		});
}

export async function verifyUser(username, password) {
	try {
		const response = await fetch(getUserUri("verifyUser", username, password), {
			method: 'POST'
		});
		return response.ok;
	} catch (error) {
		console.error("Error al verificar usuario:", error);
		return false;
	}
}

export function apiAddUsuario(nombre, apellidos, email, usuario, country, password, presentacion, foto) {
	let formData = new FormData();
	formData.append("nombre", nombre.value);
	formData.append("apellidos", apellidos.value);
	formData.append("email", email.value);
	formData.append("usuario", usuario.value);
	formData.append("pais", country.value);
	formData.append("contra", password.value);
	formData.append("presentacion", presentacion ? presentacion.value : "");

	if (foto instanceof File) { formData.append("foto", foto) }

	fetch("/facefood/api/usuarios", {
		method: "POST",
		body: formData
	})
		.then(async (response) => {
			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "Error en el registro");
			}
			return response.json();
		})
		.then((data) => {
			sessionStorage.setItem("usuario", JSON.stringify(data));
			successSwalTimWeb('Usuario Registrado', 'Puedes empezar a guardar tus recetas 🍳', `/facefood/${usuario.value}`)
		})
		.catch((error) => {
			console.error("Error: ", error);
			errorSwalPag("Error", error.message, `/facefood/`);
		});
}

export function apiUpdateUsuario(usuarioSession, nombre, apellidos, email, usuario, country, password, presentacion, avatar) {
	let formData = new FormData();
	formData.append("id", usuarioSession.id);
	formData.append("nombre", nombre.value);
	formData.append("apellidos", apellidos.value);
	formData.append("email", email.value);
	formData.append("usuario", usuario.value);
	formData.append("pais", country.value);
	formData.append("presentacion", presentacion ? presentacion.value : "");
	formData.append("contra", password.value);
	if (avatar instanceof File) { formData.append("foto", avatar) }
	fetch(getUserUri("updateUser", usuarioSession.id), {
		method: "PUT",
		body: formData
	})
		.then(async (response) => {
			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "Error en la actualización");
			}
			return response.json();
		})
		.then((data) => {
			sessionStorage.setItem("usuario", JSON.stringify(data));
			successSwalTimWeb('Usuario Actualizado', 'Puedes seguir guardando tus recetas 🍳', `/facefood/${data.usuario}`)
		})
		.catch((error) => {
			console.error("Error: ", error);
			errorSwalPag("Error", error.message);
		});
}

export function apiDeleteUsuario(idUsuario) {
	fetch(getUserUri("deleteRecipe", idUsuario), {
		method: "DELETE",
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error("Error al eliminar el usuario");
			}
			setUserOut()
			successSwalTimWeb('Usuario Eliminado', 'Esperamos verte por aquí de nuevo pronto 🍳', `/facefood/`);
		})
		.catch((error) => {
			console.error("Error al borrar receta:", error);
			errorSwalPag("Error", error.message);
		});
}

export async function checkAvailability(uri, atribute) {
	try {
		const response = await fetch(getUserUri(uri, atribute));
		if (response.status === 404) {
			return true
		} else {
			return false
		}
	} catch (error) {
		return false; 
	}
}

export function apiAddRecipe(titulo, idPais, duracion, personas, presentacion, foto, ingredientes, listPasos) {
	let formData = new FormData();
	formData.append("titulo", titulo.value);
	formData.append("idPais", idPais.value);
	formData.append("duracion", duracion.value);
	formData.append("personas", personas.value);
	formData.append("presentacion", presentacion.value);
	ingredientes.forEach(i => {
		formData.append("ingredientes", `${i[0]}|${i[1]}`);
	});
	listPasos.forEach((paso) => {
		// paso[0] = explicacion, paso[1] = File
		formData.append("pasos", paso[0]); // Esto se convierte en lista de strings
		if (paso[1] instanceof File) {
			formData.append("fotosPasos", paso[1]); // Esto será lista de MultipartFile
		} else {
			formData.append("fotosPasos", new Blob([], { type: "image/jpeg" })); // placeholder vacío si no hay imagen
		}
	});
	if (foto instanceof File) {
		formData.append("foto", foto);
	}
	const usuario = JSON.parse(sessionStorage.getItem("usuario"))
	sessionStorage.removeItem("listIng")

	fetch(getRecipeUri("add", usuario.id), {
		method: "POST",
		body: formData
	})
		.then(async (response) => {
			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "Error al añadir la receta");
			}
			return response.json();
		})
		.then((data) => {
			successSwalTimWeb('Receta Añadida', 'Ya la tienes en tu lista 🍳', `/facefood/${usuario.usuario}`)
		})
		.catch((error) => {
			console.error("Error: ", error);
			errorSwalPag("Error", error.message);
		});
}

export function apiSaveRecipe(titulo, idPais, duracion, personas, presentacion, foto, ingredientes, listPasos, isUpdate, recetaId = null) {
	let formData = new FormData();
	formData.append("titulo", titulo.value);
	formData.append("idPais", idPais.value);
	formData.append("duracion", duracion.value);
	formData.append("personas", personas.value);
	formData.append("presentacion", presentacion.value);

	ingredientes.forEach(i => {
		formData.append("ingredientes", `${i[0]}|${i[1]}`);
	});

	listPasos.forEach((paso) => {
		formData.append("pasos", paso[0]);
		if (paso[1] instanceof File) {
			formData.append("fotosPasos", paso[1]);
		} else if (paso[1] === "DELETE") {
			// Mandar marcador de borrado
			const blobDelete = new Blob([], { type: "text/plain" });
			formData.append("fotosPasos", blobDelete, "DELETE");
		} else {
			// Mandar blob vacío para mantener el orden (pasos sin imagen)
			const blobEmpty = new Blob([], { type: "text/plain" });
			formData.append("fotosPasos", blobEmpty, "NOFILE");
		}
	});

	if (foto instanceof File) {
		formData.append("foto", foto);
	}

	const usuario = JSON.parse(sessionStorage.getItem("usuario"));
	const recetaSession = getRecipe();

	sessionStorage.removeItem("listIng");

	let url = "";
	let method = "";

	if (isUpdate) {
		url = getRecipeUri("updateRecipe", usuario.id, recetaId || recetaSession.id);
		method = "PUT";
	} else {
		url = getRecipeUri("add", usuario.id);
		method = "POST";
	}

	fetch(url, {
		method: method,
		body: formData
	})
		.then(async (response) => {
			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "Error al guardar receta");
			}
			return response.json();
		})
		.then((data) => {
			successSwalTimWeb(
				isUpdate ? 'Receta Actualizada' : 'Receta Creada',
				isUpdate ? 'Tu receta se actualizó 🍽️' : 'Tu receta se guardó 🍽️',
				`/facefood/${usuario.usuario}`
			);
		})
		.catch((error) => {
			console.error("Error: ", error);
			errorSwalPag("Error", error.message);
		});
}

export function apiDeleteRecipe(idReceta) {
	const usuario = JSON.parse(sessionStorage.getItem("usuario"))

	fetch(getRecipeUri("deleteRecipe", idReceta), {
		method: "DELETE",
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error("Error al eliminar la receta");
			}
			successSwalTimWeb('Receta Eliminada', 'Sigue mirando recetas 🍳', `/facefood/${usuario.usuario}`);
		})
		.catch((error) => {
			console.error("Error al borrar receta:", error);
			errorSwalPag("Error", error.message);
		});
}


export async function cargarRecetas(uri, usuario) {
	const recipeList = document.getElementById("recipe-grid");
	recipeList.innerHTML = ""; 

	let response = null
	let idUsuario = -1;
	if (userIsLogged(usuario)) {
		idUsuario = usuario.id;
	}
	const fullUri = `${uri}?idUsuario=${idUsuario}`;
	response = await fetch(fullUri); 

	// Llamamos al backend
	const recetas = await response.json(); 

	if (!recetas || recetas.length === 0) {
		recipeList.innerHTML = `<h2 class="title-empty">No hay recetas</h2>`;
	} else {
		recetas.forEach((receta, index) => {
			const card = document.createElement("div");
			card.className = "recipe-card";
			card.classList.add("fade-slide-in")
			card.style.animationDelay = `${index * 100}ms`;

			let pais = getFlagFileName(receta.paisDTO.nombre);

			// Generar estrellas según la calificación
			let rating = receta.puntuacion || 0; 
			let estrellasHTML = generarEstrellas(rating);

			// Icono de favorito
			let favIcon = receta.favorita ? "fav" : "emptyheart";

			//duracion
			let duracion = (receta.duracion == "70") ? "+60" : receta.duracion

			//mostrar o no el icono de favorito
			let display = "block";
			if (!userIsLogged(usuario)) {
				display = "none";
			}

			card.innerHTML = `
				            <img id="${receta.id}" class="recipe-img" src="${receta.foto}" alt="${receta.titulo}" title="${receta.titulo}"" loading="lazy">
				            <img src="img/flags/${pais}.png" class="flag" alt="${receta.paisDTO.nombre}" title="${receta.paisDTO.nombre}" onerror="this.onerror=null; this.src='img/flags/default.png'">
							<div class="recipe-info">
								<p class="recipe-title">${receta.titulo}</p>
								<div class="recipe-rating">${estrellasHTML} <span>${receta.puntuacion}</span></div>
								<div class="fx-cc">
									<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(0, 0, 0, 1);transform: ;msFilter:;"><path d="M12 2C6.486 2 2 6.486 2 12s4.486 10 10 10 10-4.486 10-10S17.514 2 12 2zm0 18c-4.411 0-8-3.589-8-8s3.589-8 8-8 8 3.589 8 8-3.589 8-8 8z"></path><path d="M13 7h-2v6h6v-2h-4z"></path></svg>
									<p class="recipe-duration">${duracion} min.</p>
									<img src="${PATH_IMG_ICON}${favIcon}.svg" loading="lazy" class="favorite-icon favorite-update" data-receta-id="${receta.id}" data-favorita="${receta.favorita}" data-uri=${uri} style="display: ${display};" alt="Favorito" title="Añadir a Favoritos">
								</div>
							</div>
				        `;
			recipeList.appendChild(card);
		});

		iniciarLazyLoading();
	}
}



export function getRecipeDetails(idRecipe) {
	const usuario = getUser()

	if (!userIsLogged(usuario)) {
		fetch(getRecipeUri("findById", idRecipe), {
			method: "GET",
			headers: { "Content-Type": "application/json" },
		})
			.then(async (response) => {
				if (!response.ok) {
					const errorText = await response.text();
					throw new Error(errorText || "No existe el Usuario");
				}
				return response.json();
			})
			.then(async (data) => {
				sessionStorage.setItem("receta", JSON.stringify(data));
				window.location.href = `/facefood/receta/${idRecipe}`;
			})
			.catch((error) => {
				console.error("Error: ", error);
				errorSwalPag("Error", error.message, `/facefood/main`);
			});
		return;
	}

	fetch(getRecipeUri("findByIdAndUser", usuario.id, idRecipe), {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	})
		.then(async (response) => {
			if (!response.ok) {
				const errorText = await response.text();
				throw new Error(errorText || "No existe el Usuario");
			}
			return response.json();
		})
		.then(async (data) => {
			sessionStorage.setItem("receta", JSON.stringify(data));
			window.location.href = `/facefood/${usuario.usuario}/receta/${idRecipe}`;
		})
		.catch((error) => {
			console.error("Error: ", error);
			errorSwalPag("Error", error.message, `/facefood/${usuario.usuario}`);
		});
}

export async function getListaRecetas(uri) {
	try {
		let usuario = getUser()
		let response = null

		let idUsuario = -1
		if (userIsLogged(usuario)) {
			idUsuario = usuario.id
		}

		const fullUri = `${getRecipeUri(uri)}?idUsuario=${idUsuario}`
		response = await fetch(fullUri)

		if (!response.ok) {
			const errorText = await response.text()
			throw new Error(errorText || "No existe la receta")
		}

		return await response.json()
	} catch (error) {
		console.error("Error: ", error)
		errorSwal("Error", error.message)
		throw error; 
	}
}

export function toggleFavorito(idReceta, usuario) {
	const datos = {
		id_usu: usuario.id,
		id_receta: idReceta,
	};

	const icono = document.querySelector(`img[data-receta-id="${idReceta}"]`);
	const isFavorita = icono.getAttribute("data-favorita") === "true";

	if (isFavorita) {
		fetch(getFavoritosUri("delete", usuario.id, idReceta), {
			method: "DELETE",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(datos),
		})
			.then(async (response) => {
				if (!response.ok) {
					const errorText = await response.text();  // Cambiar json() a text()
					throw new Error(errorText || "Error al cambiar favoritos");
				}
				icono.src = `${PATH_IMG_ICON}emptyheart.svg`;
				icono.setAttribute("data-favorita", "false");

				let recetaSession = JSON.parse(sessionStorage.getItem("receta"));
				if (recetaSession) {
					recetaSession.favorita = false;
					sessionStorage.setItem("receta", JSON.stringify(recetaSession));
				}
			})
			.catch((error) => {
				console.error("Error: ", error);
				errorSwal("Error", error.message);
			});

	} else {
		fetch(getFavoritosUri("add", usuario.id, idReceta), {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(datos),
		})
			.then(async (response) => {
				if (!response.ok) {
					const errorText = await response.text();  // Cambiar json() a text()
					throw new Error(errorText || "Error al cambiar favoritos");
				}
				icono.src = `${PATH_IMG_ICON}fav.svg`;
				icono.setAttribute("data-favorita", "true");

				let recetaSession = JSON.parse(sessionStorage.getItem("receta"));
				if (recetaSession) {
					recetaSession.favorita = false;
					sessionStorage.setItem("receta", JSON.stringify(recetaSession));
				}
			})
			.catch((error) => {
				console.error("Error: ", error);
				errorSwal("Error", error.message);
			});
	}
}

export async function getIngredientes(idRecipe) {
	try {
		const response = await fetch(getIngredienteUri("findAllByIdRecipe", idRecipe), {
			method: "GET",
			headers: { "Content-Type": "application/json" },
		});
		if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || "No existe la receta");
		}
		return await response.json();
	} catch (error) {
		console.error("Error: ", error);
		errorSwal("Error", error.message);
		throw error; 
	}
}

export async function getPasos(idRecipe) {
	try {
		const response = await fetch(getPasoUri("findAllByIdRecipe", idRecipe), {
			method: "GET",
			headers: { "Content-Type": "application/json" },
		});
		if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || "No existe el Usuario");
		}
		return await response.json();
	} catch (error) {
		console.error("Error: ", error);
		errorSwalPag("Error", error.message, `/facefood/`);
		throw error; 
	}
}

export async function findAllIngredientes() {
	try {
		const response = await fetch(getIngredienteUri("findAll"), {
			method: "GET",
			headers: { "Content-Type": "application/json" },
		});
		if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || "No existen ingrendientes");
		}
		return await response.json();
	} catch (error) {
		console.error("Error: ", error);
		errorSwal("Error", error.message);
		throw error; 
	}
}

export async function findAllPaises(countrySelect) {
	try {
		const response = await fetch("/facefood/api/paises"); 
		const paises = await response.json(); 
		countrySelect.innerHTML = '<option value="-1" disabled selected>Selecciona un país</option>';
		paises.forEach(pais => {
			const option = document.createElement("option");
			option.value = pais.id;
			option.textContent = pais.nombre;
			countrySelect.appendChild(option);
		});
	} catch (error) {
		console.error("Error al obtener los países", error);
	}
}

export async function setRating(ratingSeleccionado, idReceta, idUsuario) {
	let formData = new FormData();
	formData.append("rate", ratingSeleccionado);
	try {
		const response = await fetch(`/facefood/api/recetas/${idUsuario}/${idReceta}/rate`, {
			method: 'POST',
			body: formData,
		});

		if (!response.ok) throw new Error("Error al enviar valoración");
		Swal.fire("¡Gracias!", "Tu valoración se ha registrado.", "success");
		const data = await response.json();
		return data;

	} catch (error) {
		console.error(error);
		Swal.fire("Error", "No se pudo enviar tu valoración.", "error");
	}
}

export async function findApiRating(idReceta, idUsuario) {

	try {
		const response = await fetch(getValoracionUri("findRating", idUsuario, idReceta), {
			method: 'GET',
		});
		if (response.status === 404) {return null}

		if (!response.ok) {
			throw new Error("Error en la petición: " + response.status);
		}

		const data = await response.json();
		return data;

	} catch (error) {
		return null;
	}
}

export async function apiRequestPassword(email) {
	const response = await fetch(getUserUri("requestPassword"), {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ email }),
	});

	Swal.fire("Email enviado", "Si estas registrado te llegará un correo de recuperación", "success");
}

export async function apiSavePassword(newPassword, token) {
	const response = await fetch(getUserUri("resetPassword"), {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify({ token, newPassword })
	});

	if (response.ok) {
		successSwalTimWeb('Contraseña actualizada', 'Ahora puedes entrar 🍳', `/facefood/login`);
	} else {
		Swal.fire("Error", "No se pudo guardar la contraseña", "error");
	}
}