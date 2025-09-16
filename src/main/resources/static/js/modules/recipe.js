import { apiSaveRecipe, findApiRating, getIngredientes, getListaRecetas, getPasos, getRecipeDetails, setRating } from "./api.js"
import { crearElemento, errorUserNoLogged, mostrarErrores, setupArrowButtons, setupScrollZones, validateFile, validateInput, validateSelect } from "./ui.js";
import { PATH_IMG_ICON, PATH_IMG_RECETAS } from "./const.js"
import { fillIngredientes } from "./ingrediente.js";
import { fillPasos } from "./paso.js";
import { getUser, userIsLogged } from "./user.js";
import { recortarImagenCuadrada, revisarYCortarImagenCuadrada } from "./photo.js";

export function getRecipe() {
	if (sessionStorage.getItem("receta")) {
		return JSON.parse(sessionStorage.getItem("receta"));
	}
	return sessionStorage.getItem("receta")
}

export function saveRecipe(recipe) {
	sessionStorage.setItem("receta", JSON.stringify(recipe));
}

export function setRecipeOut() {
	sessionStorage.setItem("receta", "");
}

export function getFlagFileName(name) {
	let nombrePais = name.toLowerCase().trim();

	const removeAccents = (str) => {
		return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
	}
	let pais = removeAccents(nombrePais);
    pais = pais.replace(/\s+/g, "");

	return pais;
}

export async function cargarIngredientes(idRecipe) {
	try {
		const listaRecetaIngrediente = await getIngredientes(idRecipe);

		let ulCant = document.getElementById("cant")
		let ulIng = document.getElementById("ing")
		let container = document.getElementById('list-ing')

		ulCant.innerHTML = "";
		ulIng.innerHTML = "";
		container.innerHTML = "";

		// Máximo de 5 ingredientes por columna
		let filas = Math.ceil(listaRecetaIngrediente.length / 5);

		for (let fil = 0; fil < filas; fil++) {
			//Crear columna
			let colDiv = crearElemento("div", container, ["icontainer"])

			// Crear nuevas listas cada 5 elementos
			let ulCant = crearElemento("ul", colDiv, ["ulcant"])
			let ulIng = crearElemento("ul", colDiv, ["uling"])

			// Agregar ingredientes en la columna actual (máximo 5)
			for (let i = fil * 5; i < (fil + 1) * 5 && i < listaRecetaIngrediente.length; i++) {

				let liCant = crearElemento("li", ulCant)
				liCant.textContent = listaRecetaIngrediente[i].cantidad;

				let liIng = crearElemento("li", ulIng)
				liIng.textContent = listaRecetaIngrediente[i].ingredienteDTO.nombre;
			}
		}

	} catch (error) {
		console.error("Error al cargar ingredientes:", error);
	}
}

// Función para generar las estrellas según la calificación
export function generarEstrellas(calificacion, isRated = false) {
	let estrellas = "";
	for (let i = 1; i <= 5; i++) {
		if (i <= calificacion) {
			if (!isRated) estrellas += `<span class="star filled" data-value="${i}"><img src="${PATH_IMG_ICON}fullstar.svg"></span>`; // Estrella llena
			else estrellas += `<span class="star filled" data-value="${i}"><img src="${PATH_IMG_ICON}star.svg"></span>`; // Estrella llena
		} else if (!isRated && (i - 0.5 <= calificacion)) {
			estrellas += `<span class="star filled" data-value="${i}"><img src="${PATH_IMG_ICON}halfstar.svg"></span>`; // Estrella a mitad
		} else {
			estrellas += `<span class="star" data-value="${i}"><img src="${PATH_IMG_ICON}staroutline.svg"></span>`; // Estrella vacía
		}
	}
	return estrellas;
}

export function activarValoracion(idReceta, idUsuario, valorInicial, isRated) {
	const stars = document.querySelectorAll("#r-rating .star");
	let ratingSeleccionado = 0;

	stars.forEach(star => {
		star.addEventListener("mouseenter", function () {
			const valor = parseInt(this.getAttribute("data-value"));
			resaltarEstrellasHover(valor);
		});

		star.addEventListener("mouseleave", function () {
			resaltarEstrellas(valorInicial, isRated);
		});

		star.addEventListener("click", async function () {
			ratingSeleccionado = parseInt(this.getAttribute("data-value"));
			resaltarEstrellasHover(ratingSeleccionado);

			if (userIsLogged(getUser())) {
				try {
					const updatedRecipe = await setRating(ratingSeleccionado, idReceta, idUsuario);
					// ACTUALIZAR estrellas y puntuación
					let stars = document.getElementById("r-rating")
					let estrellasHTML = generarEstrellas(ratingSeleccionado, isRated)
					stars.innerHTML = `${estrellasHTML} <span>${updatedRecipe.puntuacion}</span>`
				} catch (error) {
					console.error(error);
					Swal.fire("Error", "No se pudo enviar tu valoración.", "error")
				}
			} else {
				errorUserNoLogged("Para valorar la receta debes iniciar sesión.")
			}

		});
	});

	function resaltarEstrellasHover(valor) {
		const currentStars = document.querySelectorAll("#r-rating .star");
		currentStars.forEach(star => {
			const starValue = parseInt(star.getAttribute("data-value"));
			const img = star.querySelector("img"); // accede al <img> dentro del <span>
			if (img) {
				if (starValue <= valor) {
					img.setAttribute("src", `${PATH_IMG_ICON}star.svg`);
				} else {
					img.setAttribute("src", `${PATH_IMG_ICON}staroutline.svg`);
				}
			}
		});
	}

	function resaltarEstrellas(valor, isRated = false) {
		const currentStars = document.querySelectorAll("#r-rating .star");
		currentStars.forEach(star => {
			const starValue = parseInt(star.getAttribute("data-value"));
			const img = star.querySelector("img"); // accede al <img> dentro del <span>
			if (img) {
				if (starValue <= valor) {
					if (isRated) img.setAttribute("src", `${PATH_IMG_ICON}star.svg`);
					else img.setAttribute("src", `${PATH_IMG_ICON}fullstar.svg`);
				} else if (!isRated && (starValue - 0.5 <= valor)) {
					img.setAttribute("src", `${PATH_IMG_ICON}halfstar.svg`);
				} else {
					img.setAttribute("src", `${PATH_IMG_ICON}staroutline.svg`);
				}
			}
		});
	}
}

export async function findRating(idReceta, idUsuario) {
	const valoracion = await findApiRating(idReceta, idUsuario);
	if (valoracion) {
		if (valoracion != null || valoracion != undefined) {
			return valoracion.calificacion;
		}
	}
	return null;
}

export function fillRecipeForm(receta) {
	document.getElementById('title').value = receta.titulo || '';
	document.getElementById('country').value = receta.paisDTO.id || '-1';
	document.getElementById('personas').value = receta.numPersonas || '-1';
	document.getElementById('duracion').value = receta.duracion || '-1';
	document.getElementById('presentacion').value = receta.explicacion || '';

	// Para mostrar la imagen en el <img id="main-image">
	if (receta.foto) {
		document.getElementById("main-image").setAttribute("src", `${receta.foto}`);
	} else {
		document.getElementById("main-image").setAttribute("src", `${PATH_IMG_RECETAS}default.jpg`);
	}
	document.getElementById('main-image-file').value = '';

	fillIngredientes(receta.listaRecetaIngredientesDTO)
	fillPasos(receta.listaPasosDTO, receta.id)
}

export async function cargarCarruselRecetas(uri, idItem) {
	const listaRecetas = await getListaRecetas(uri);
	const contenedor = document.getElementById(idItem);

	if (!listaRecetas || listaRecetas.length === 0) {
		console.warn("No hay recetas para el carrusel");
		return;
	}

	let limit = 10
	if (idItem == "carousel-sugerencias") limit = 5
	for (let i = 0; i < limit; i++) {
		const receta = listaRecetas[i];
		const card = document.createElement("div");
		card.classList.add("card-receta");
		card.style.animationDelay = `${i * 100}ms`;

		card.innerHTML = `
			<img src="${receta.foto}" alt="${receta.titulo}" id="${receta.id}" class="cursor">
			<div class="card-body">
				<div class="card-title">${receta.titulo}</div>
				<div class="card-desc">${receta.explicacion?.slice(0, 80) || "Sin descripción..."}</div>
			</div>
		`;

		card.addEventListener("click", function () {
			saveRecipe(receta)
			getRecipeDetails(receta.id)

			window.location.href = `/facefood/receta/${receta.id}`;
		})

		contenedor.appendChild(card);
	}

	//setupScrollZones();
	setupArrowButtons();
}

export async function validateYApiRecipeForm(recetaSession) {

	let errors = []
	let title = document.getElementById("title")
	let duracion = document.getElementById("duracion")
	let personas = document.getElementById("personas")
	let presentacion = document.getElementById("presentacion")
	let countrySelect = document.getElementById("country")
	let mainImg = document.getElementById("main-image-file")
	let listIng = sessionStorage.getItem("listIng")
	//comprobar si está en pantalla de editar
	const isUpdate = window.location.href.includes("/update");

	validateInput(title, errors, "Introduce un título")
	validateSelect(countrySelect, errors, "Selecciona un país")
	validateSelect(duracion, errors, "Introduce una duración")
	validateSelect(personas, errors, "Introduce un número de personas")
	validateInput(presentacion, errors, "Introduce una presentación")
	if (!isUpdate) validateFile(mainImg, document.getElementById("src-file1"), errors, "Selecciona una imagen principal")

	if (listIng && listIng !== "undefined" && listIng !== "" && listIng !== "[]") {
		listIng = JSON.parse(listIng);
	} else {
		errors.push({ field: document.getElementById("ing-select"), message: "Añade al menos un ingrediente" });
	}

	// Validar todos los pasos
	let pasoDivs = document.querySelectorAll("#pasos-container .pasoitem");
	let listPasos = []
	for (let index = 0; index < pasoDivs.length; index++) {
		const pasoDiv = pasoDivs[index];
		let textarea = pasoDiv.querySelector(".textarea");
		let fileInput = pasoDiv.querySelector("input[type='file']");

		if (!textarea || textarea.value.trim() === "") {
			errors.push({ field: textarea, message: `El paso ${index + 1} debe tener una descripción.` });
			continue;
		}

		let texto = textarea.value.trim();

		if (fileInput && fileInput.files.length !== 0) {
			let file = fileInput.files[0];
			let ext = file.name.split(".").pop().toLowerCase();

			if (!["jpg", "jpeg", "png"].includes(ext)) {
				errors.push({ field: fileInput, message: `El paso ${index + 1} debe tener una imagen válida.` });
			}

			try {
				let fileRecortada = await recortarImagenCuadrada(file);
				listPasos.push([texto, fileRecortada]);
			} catch (err) {
				console.error("Error al recortar imagen del paso:", err);
				listPasos.push([texto, file]);
			}
		} else if (pasoDiv.getAttribute('data-foto-borrada') === 'true') {
			listPasos.push([texto, "DELETE"]);
		} else {
			listPasos.push([texto, ""]);
		}
	}

	let mainImgRecortada = null

	if (mainImg.files.length > 0) {
		mainImgRecortada = await revisarYCortarImagenCuadrada(mainImg,document.getElementById("src-file1"),errors,"Selecciona una imagen principal");
	}


	if (errors.length > 0) {
		mostrarErrores(errors)
	} else {
		btnAddRecipe.disabled = true;
		btnAddRecipe.textContent = "CARGANDO...";
		document.getElementById("loading-msg").style.display = "inline";
		// Recargar la lista actualizada de ingredientes
		listIng = sessionStorage.getItem("listIng");

		if (listIng && listIng !== "undefined" && listIng !== "") {
			listIng = JSON.parse(listIng);
		} else {
			listIng = []; // Si está vacío, no pasa nada
		}

		if (!isUpdate) {
			apiSaveRecipe(title, countrySelect, duracion, personas, presentacion, mainImgRecortada, listIng, listPasos, false);
		} else {
			const isUpdate = window.location.href.includes("/update");
			const recetaId = recetaSession.id;

			Swal.fire({
				title: isUpdate ? '¿Actualizar receta?' : '¿Registrar nueva receta?',
				text: isUpdate ? "Vas a actualizar tu receta existente." : "Vas a registrar una receta nueva.",
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: isUpdate ? 'Sí, actualizar' : 'Sí, registrar',
				cancelButtonText: 'Cancelar'
			}).then((result) => {
				if (result.isConfirmed) {
					apiSaveRecipe(
						title,countrySelect,duracion,personas,presentacion,mainImgRecortada,listIng,listPasos,isUpdate,recetaId
					);
				}
			});
		}
	}
}

