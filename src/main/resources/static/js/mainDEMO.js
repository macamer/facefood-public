
import "./modules/menu.js"
import "./modules/redirect.js"
import { fillUserData, getUser, userIsLogged, validateYApiUsuarioForm } from "./modules/user.js"
import { getRecipe, getFlagFileName, cargarIngredientes, generarEstrellas, activarValoracion, findRating, fillRecipeForm, cargarCarruselRecetas, validateYApiRecipeForm } from "./modules/recipe.js"
import { apiAddRecipe, apiAddUsuario, apiDeleteRecipe, apiDeleteUsuario, apiLogin, apiRequestPassword, apiSavePassword, apiSaveRecipe, cargarRecetas, findAllPaises, getListaRecetas, getRecipeDetails, toggleFavorito, verifyUser } from "./modules/api.js"
import { aplicarClases, crearElemento, errorSwal, errorSwalPag, errorUserNoLogged, limpiarErrores, mostrarError, mostrarErrores, setupScrollZones, successSwalTimer, toggleContraseña, validateFile, validateInput, validateList, validateSelect } from "./modules/ui.js"
import { getRecipeUri } from "./modules/uriBuilder.js"
import { PATH_IMG_AVATAR, PATH_IMG_FLAG, PATH_IMG_ICON, PATH_IMG_RECETAS } from "./modules/const.js"
import { listIngredientes, showAllIngredientes } from "./modules/ingrediente.js"
import { cambiarImagenInput, mostrarAvatar, recortarImagenCuadrada, revisarYCortarImagenCuadrada, showNameAndValiteFiles, showNameAndValiteFilesForInput } from "./modules/photo.js"
import { addPasoContainer, removePasoContainer, cargarPasos, } from "./modules/paso.js"
import { cargarCarruselRecetasDEMO } from "./modules/demoUser.js"

document.addEventListener("DOMContentLoaded", async function () {
	const usuarioSession = getUser()
	const recetaSession = getRecipe()
	sessionStorage.removeItem("listIng")

	const currentPage = window.location.pathname;
	const paginasProtegidas = ["/recipe.html", "/profile.html", "/dashboard.html"];
	const paginasSinSesion = ["/facefood/login", "/facefood/add"];

	//form
	const usuarioInput = document.getElementById("user");
	const passInput = document.getElementById("pass");
	const btnLogin = document.getElementById("btnLogin");

	const btnRegister = document.getElementById("btnRegister");
	const btnAddRecipe = document.getElementById("btnAddRecipe");

	const togglePassword = document.getElementById('toggle-password');
	const passwordInput = document.getElementById('password');

	//select con paises
	const countrySelect = document.getElementById("country");
	//select con ingredientes
	const ingSelect = document.getElementById("ing-select")

	//sidebar con Info Usuario
	let search = document.getElementById("search");

	//MENU MAIN
	let myRecipes = document.getElementById("recetas");
	let sugerencias = document.getElementById("sugerencias");
	let favoritos = document.getElementById("favoritos");
	let recipegrid = document.getElementById("recipe-grid");

	//Recipe
	let recipe = document.getElementById("recipe")

	//añadir-actualizar receta
	let addpaso = document.getElementById("add-paso")
	let adding = document.getElementById("add-ing")

	//--VERIFY PAGES--	
	if (paginasProtegidas.includes(currentPage) && !userIsLogged(usuarioSession)) {
		errorSwalPag("Ooops..", "Debes iniciar sesión", `/facefood/`);
	}

	if (paginasSinSesion.includes(currentPage) && userIsLogged(usuarioSession)) {
		errorSwalPag("Ooops..", "Ya has iniciado sesión", `/facefood/${usuarioSession.usuario}`);
	}

	let inputArchivo = document.getElementById("main-image-file");
	let imageArchivo = document.getElementById("main-image");
	const avatarImg = document.getElementById("avatar");

	if (avatarImg) mostrarAvatar(avatarImg, usuarioSession)
	if (inputArchivo != null) cambiarImagenInput(inputArchivo, imageArchivo)

	//---- Login ----
	if (btnLogin != null) {
		btnLogin.addEventListener("click", async function (e) {
			e.preventDefault();
			limpiarErrores();

			let errors = [];

			if (usuarioInput.value === "" || usuarioInput.value === null) {
				errors.push({ field: usuarioInput, message: "Introduce el nombre de usuario" });
			}

			if (passInput.value === "") {
				errors.push({ field: passInput, message: "Introduce una contraseña" });
			}

			if (errors.length > 0) {
				mostrarErrores(errors);
			} else {
				apiLogin(usuarioInput, passInput);
			}
		});

		document.getElementById("forgot").addEventListener("click", async function (e) {
			e.preventDefault();
			Swal.fire({
				title: '¿Olvidaste la contraseña?',
				text: "Enviar enlace de recuperación",
				icon: 'warning',
				input: 'email',
				inputPlaceholder: 'Email',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Enviar Correo',
				cancelButtonText: 'Cancelar',
				inputValidator: (value) => {
					if (!value) {
						return 'Debes introducir tu email';
					}
				}
			}).then(async (result) => {
				if (result.isConfirmed) {
					const email = result.value;
					var emailexpreg = /[\w-\.]{3,}@([\w-]{2,}\.)*([\w-]{2,}\.)[\w-]{2,4}/;
					if (!emailexpreg.test(email)) {
						errorSwal("Error", "Email no válido");
						return
					}
					apiRequestPassword(email);
				}
			});
		})
	}

	//--INDEX--
	// if (document.getElementById("gallery") != null) {
	// 	async function cargarGaleria() {
	// 		const listaRecetas = await getListaRecetas("findBestRecetas");

	// 		if (!listaRecetas || listaRecetas.length === 0) {
	// 			console.warn("No hay recetas para mostrar");
	// 			return;
	// 		}

	// 		for (let i = 0; i < 6; i++) {
	// 			const receta = listaRecetas[i];

	// 			let img = crearElemento("img", document.getElementById("gallery"), ["img-fluid", "rounded-circle"]);
	// 			img.setAttribute("src", `${receta.foto}`)
	// 			img.setAttribute("alt", receta.titulo)
	// 			img.setAttribute("name", receta.titulo)
	// 			img.setAttribute("id", receta.id)
	// 			img.setAttribute("data-uri", getRecipeUri("findReceta", receta.id))
	// 			img.setAttribute("data-receta-id", receta.id)
	// 			img.setAttribute("loading", "lazy")
	// 		}
	// 	}

	// 	cargarGaleria(); // Llamamos a la función async
	// }

	if (document.getElementById("carousel-recetas") != null) {
		cargarCarruselRecetasDEMO("findBestRecetas", "carousel-recetas");
		cargarCarruselRecetasDEMO("findRandom", "carousel-sugerencias");
	}

	//MOSTRAR toggle contrasenya
	if (togglePassword != null) toggleContraseña(togglePassword, passwordInput)

	//---MOSTRAR PAISES---
	if (countrySelect != null) findAllPaises(countrySelect)

	//--MOSTRAR INGREDIENTES
	if (ingSelect != null) showAllIngredientes(ingSelect)

	//---AÑADIR USUARIO---
	if (btnRegister != null) {
		if (!userIsLogged(usuarioSession)) {
			btnRegister.addEventListener("click", async function (e) {
				e.preventDefault();
				limpiarErrores();

				validateYApiUsuarioForm(usuarioSession)
			})
		}
	}

	/*limpiar buscador*/
	if (document.getElementById("reset-search") != null) {
		document.getElementById("reset-search").addEventListener("click", function (e) {
			e.preventDefault();
			let searcher = document.getElementById('buscador')
			searcher.value = "";
			document.getElementById("order").value = -1
			let idpais = document.getElementById("country")
			idpais.value = -1
			aplicarClases(sugerencias)
			cargarRecetas(getRecipeUri("findAll"), usuarioSession);
		});
	}

	//MOSTRAR RECETAS MAIN
	if (sugerencias != null) {

		if (userIsLogged(usuarioSession)) {
			aplicarClases(sugerencias)
			cargarRecetas(getRecipeUri("findAll"), usuarioSession)

			// Cargar sugerencias
			sugerencias.addEventListener('click', async () => {
				aplicarClases(sugerencias)
				cargarRecetas(getRecipeUri("findAll"), usuarioSession)
			});

			// Cargar mis recetas
			myRecipes.addEventListener('click', async () => {
				aplicarClases(myRecipes)
				cargarRecetas(getRecipeUri("findMyRecipes", usuarioSession.id), usuarioSession)
			});

			// Cargar favoritos
			favoritos.addEventListener('click', async () => {
				aplicarClases(favoritos)
				cargarRecetas(getRecipeUri("findUserFav", usuarioSession.id), usuarioSession)
			});
		} else {
			// Si no hay usuario, cargar recetas sugeridas
			cargarRecetas(getRecipeUri("findAll"));
			myRecipes.style.display = "none"
			favoritos.style.display = "none"
			document.getElementById("addrecipe").style.display = "none"
			document.getElementById("menu").classList.remove("g-col-4")
			document.getElementById("menu").classList.add("fx-csb")
		}

		// Cargar buscador
		search.addEventListener('click', async (e) => {
			e.preventDefault();
			aplicarClases(buscador)
			let searcher = document.getElementById('buscador')
			let order = document.getElementById("order")
			let idpais = document.getElementById("country")

			limpiarErrores();
			if (searcher.value != "") {

				if (idpais.value != -1) {
					if (order.value != -1) {
						if (order.value == 1) cargarRecetas(getRecipeUri("findBestRecetasByWordsAndCountry", searcher.value, idpais.value), usuarioSession)
						else if (order.value == 2) cargarRecetas(getRecipeUri("findNewRecetasByWordsAndCountry", searcher.value, idpais.value), usuarioSession)
						else if (order.value == 3) cargarRecetas(getRecipeUri("findOldRecetasByWordsAndCountry", searcher.value, idpais.value), usuarioSession)
					} else cargarRecetas(getRecipeUri("findByWordsAndCountry", searcher.value, idpais.value), usuarioSession)

				} else if (order.value != -1) {
					if (order.value == 1) cargarRecetas(getRecipeUri("findBestRecetasByWords", searcher.value), usuarioSession)
					else if (order.value == 2) cargarRecetas(getRecipeUri("findNewRecetasByWords", searcher.value), usuarioSession)
					else if (order.value == 3) cargarRecetas(getRecipeUri("findOldRecetasByWords", searcher.value, idpais.value), usuarioSession)
				} else {
					cargarRecetas(getRecipeUri("findByWords", searcher.value), usuarioSession)
				}

			} else if (idpais.value != -1) {

				if (order.value != -1) {
					if (order.value == 1) cargarRecetas(getRecipeUri("findBestRecetasByCountry", idpais.value), usuarioSession)
					if (order.value == 2) cargarRecetas(getRecipeUri("findNewRecetasByCountry", idpais.value), usuarioSession)
					if (order.value == 3) cargarRecetas(getRecipeUri("findOldRecetasByCountry", idpais.value), usuarioSession)
				} else {
					cargarRecetas(getRecipeUri("findByCountry", idpais.value), usuarioSession)
				}

			} else if (order.value != -1) {
				if (order.value == 1) cargarRecetas(getRecipeUri("findBestRecetas"), usuarioSession)
				if (order.value == 2) cargarRecetas(getRecipeUri("findNewRecetas"), usuarioSession)
				if (order.value == 3) cargarRecetas(getRecipeUri("findOldRecetas"), usuarioSession)
			}

		});
	}

	if (recipegrid) {
		recipegrid.addEventListener("click", function (event) {
			const target = event.target;

			// Clic en el icono de favorito
			if (target && target.classList.contains("favorite-icon")) {
				event.preventDefault();
				event.stopPropagation();
				const recetaId = target.getAttribute("data-receta-id");
				const favorita = target.getAttribute("data-favorita") === "true";
				toggleFavorito(recetaId, usuarioSession);
				return;
			}

			// Clic en la imagen de receta o en el overlay de info
			if (target && (target.classList.contains("recipe-img") || target.classList.contains("recipe-info"))) {
				event.preventDefault();
				event.stopPropagation();
				let recetaId = "";

				// Si es imagen, id está en el target
				if (target.classList.contains("recipe-img")) {
					recetaId = target.getAttribute("id");
				} else {
					// Si es el overlay .recipe-info, el id está en su hermano anterior (la img.recipe-img)
					const img = target.parentElement.querySelector(".recipe-img");
					recetaId = img.getAttribute("id");
				}

				getRecipeDetails(recetaId);
				return;
			}

			event.preventDefault();
			event.stopPropagation();
		});

	}

	if (document.getElementById("favorite-icon-detail")) {

		document.getElementById("favorite-icon-detail").addEventListener("click", function (event) {
			event.stopPropagation();
			if (!userIsLogged(usuarioSession)) {
				errorUserNoLogged("Debes iniciar sesión para añadir a favoritos");
				return;
			}
			toggleFavorito(recetaSession.id, usuarioSession);
		});
	}

	// MOSTRAR INFO RECETA
	if (recipe) {
		const recetaSession = getRecipe()
		//bandera
		let flag = document.getElementById("r-flag")
		let flagFile = getFlagFileName(recetaSession.paisDTO.nombre)
		flag.setAttribute("src", `${PATH_IMG_FLAG}${flagFile}.png`)
		flag.setAttribute("alt", `${recetaSession.paisDTO.nombre}`)
		flag.name = recetaSession.paisDTO.nombre

		flag.onerror = function () {
			this.onerror = null; 
			this.src = `${PATH_IMG_FLAG}default.png`; 
			this.alt = recetaSession.paisDTO.nombre + " (no disponible)";
		};

		//actualizar favorito
		let favIconDetail = document.getElementById("favorite-icon-detail");
		favIconDetail.setAttribute("data-receta-id", recetaSession.id);
		favIconDetail.setAttribute("data-favorita", recetaSession.favorita);
		if (recetaSession.favorita) {
			favIconDetail.setAttribute("src", `${PATH_IMG_ICON}fav.svg`);
		}

		//calificacion
		let stars = document.getElementById("r-rating")
		let puntuacion = recetaSession.puntuacion
		let isRated = false
		if (usuarioSession != null) {
			const puntuacionUsuario = await findRating(recetaSession.id, usuarioSession.id);

			if (puntuacionUsuario !== null && puntuacionUsuario !== 0) {
				puntuacion = puntuacionUsuario;
				isRated = true;
			}
		}
		let estrellasHTML = generarEstrellas(puntuacion, isRated);
		stars.innerHTML = `${estrellasHTML} <span>${recetaSession.puntuacion}</span>`
		if (usuarioSession != null) activarValoracion(recetaSession.id, usuarioSession.id, puntuacion, isRated)

		//mostrar icon editar
		if (userIsLogged(usuarioSession) && usuarioSession.id == recetaSession.usuarioDTO.id) {
			document.getElementById("editrecipe").style.opacity = 1;
			document.getElementById("deleterecipe").style.opacity = 1;

			document.getElementById("deleterecipe").addEventListener("click", function (e) {
				e.preventDefault()
				Swal.fire({
					title: '¿Eliminar receta?',
					text: "No podrás recuperar la receta después de eliminarla.",
					icon: 'warning',
					showCancelButton: true,
					confirmButtonColor: '#3085d6',
					cancelButtonColor: '#d33',
					confirmButtonText: 'Sí, eliminar',
					cancelButtonText: 'Cancelar'
				}).then((result) => {
					if (result.isConfirmed) {
						apiDeleteRecipe(recetaSession.id)
					}
				})
			})

		}

		//titulo
		document.getElementById("title").innerHTML = recetaSession.titulo
		//personas
		document.getElementById("pers").innerHTML = `${recetaSession.numPersonas} pers.`
		//tiempo
		let duracion = (recetaSession.duracion == "70") ? "+60" : recetaSession.duracion
		document.getElementById("temp").innerHTML = `${duracion} min.`
		//explicacion
		document.getElementById("explicacion").innerHTML = recetaSession.explicacion
		//usuario creador
		document.getElementById("u-img").setAttribute("src", `${recetaSession.usuarioDTO.avatar}`)
		document.getElementById("u-img").name = recetaSession.usuarioDTO.avatar

		document.getElementById("usuario").innerHTML = recetaSession.usuarioDTO.usuario
		document.getElementById("u-pres").innerHTML = recetaSession.usuarioDTO.presentacion

		//usuario logged
		document.getElementById("r-main-img").setAttribute("src", `${recetaSession.foto}`)
		document.getElementById("r-main-img").name = recetaSession.titulo

		// Llamar a la función para cargar ingredientes
		cargarIngredientes(recetaSession.id);
		cargarPasos(recetaSession.id)
	}

	let stepNumber = 2; // Inicializamos el contador de pasos

	if (addpaso) addPasoContainer(addpaso, stepNumber)
	if (document.querySelectorAll('input[type="file"]').length > 0) {
		document.querySelectorAll('input[type="file"]').forEach(input => {
			input.addEventListener('change', function () {
				showNameAndValiteFilesForInput(this);
			});
		});
	}

	let removepaso = document.getElementById("remove-paso")
	if (removepaso) removePasoContainer(removepaso)



	//AÑADIR INGREDIENTES
	if (adding) {
		adding.addEventListener("click", function () {

			let ingcontainer = document.getElementById("list-ing")

			let ingSesionStr = sessionStorage.getItem("listIng");
			let ingSesion = [];
			try {
				if (ingSesionStr && ingSesionStr !== "undefined" && ingSesionStr !== "") {
					ingSesion = JSON.parse(ingSesionStr);
					if (!Array.isArray(ingSesion)) {
						ingSesion = []; // Si lo que hay no es un array, lo ignoramos
					}
				}
			} catch (e) {
				console.warn("Error al parsear sessionStorage:", e);
				ingSesion = [];
			}

			const ingSelect = document.getElementById("ing-select");
			const cant = document.getElementById("cant");
			const tipo = document.getElementById("tipo");
			const inputOtro = document.getElementById("inputOtro")
			const checkOtro = document.getElementById("addOtro")

			let idIng = ""
			let nombre = ""

			if (inputOtro.disabled) {
				idIng = ingSelect.value
				nombre = ingSelect.options[ingSelect.selectedIndex].text;
			} else {
				nombre = inputOtro.value
			}

			const cantidad = cant.value;
			const unidad = tipo.value;



			limpiarErrores(); // Limpia errores previos
			let errors = [];

			if (!checkOtro.checked) {
				validateSelect(ingSelect, errors, "Elige un ingrediente")
			} else {
				validateInput(inputOtro, errors, "Indica el nuevo ingrediente")
			}
			validateInput(cant, errors, "Indica la cantidad")
			validateSelect(tipo, errors, "Indica la cantidad")

			if (errors.length > 0) {
				mostrarErrores(errors);
			} else {
				const identificador = idIng || nombre;

				const yaExiste = ingSesion.some(([nombreExistente]) => nombreExistente === identificador);

				if (yaExiste) {
					errors.push({
						field: !checkOtro.checked ? ingSelect : inputOtro,
						message: "El ingrediente ya está en la lista"
					});
					mostrarErrores(errors);
					return;
				}
				let cantMedida = `${cantidad} ${unidad}`
				listIngredientes(idIng, nombre, cantMedida, ingcontainer)
				// Creamos el array del ingrediente actual
				let ingCant;
				if (idIng !== "") {
					ingCant = [idIng, cantMedida];
				} else {
					ingCant = [nombre, cantMedida];
				}

				// Lo añadimos a la lista
				ingSesion.push(ingCant);

				// Guardamos de nuevo
				sessionStorage.setItem("listIng", JSON.stringify(ingSesion));
			}
		})
	}

	//---REGISTRAR RECETA---
	if (btnAddRecipe != null) {
		btnAddRecipe.addEventListener("click", async function (e) {
			e.preventDefault()
			limpiarErrores()

			validateYApiRecipeForm(recetaSession)	
		});
	}

	/* HABILITAR Y DESHABILITAR INPUT OTRO */
	if (document.getElementById("addOtro")) {
		document.getElementById("addOtro").addEventListener('change', function () {
			if (this.checked) {
				document.getElementById("inputOtro").disabled = false
				document.getElementById("ing-select").disabled = true
			} else {
				document.getElementById("inputOtro").disabled = true
				document.getElementById("ing-select").disabled = false

			}
		});
	}

	//--Redirigir a Editar receta
	const path = window.location.pathname;
	const regexEdit = /\/receta\/(\d+)\/update$/;
	const match = path.match(regexEdit);

	if (match) {
		const recetaId = match[1];

		try {
			const response = await fetch(`/facefood/api/recetas/receta/${recetaId}`);
			if (!response.ok) {
				throw new Error('Error al cargar receta');
			}
			const receta = await response.json();
			fillRecipeForm(receta); 
			document.getElementById('btnAddRecipe').textContent = 'ACTUALIZAR RECETA';
			document.getElementById('h2').textContent = 'ACTURALIZAR RECETA';
		} catch (error) {
			console.error('Error:', error);
		}
	} else {
		if (document.getElementById('btnAddRecipe')) {
			document.getElementById('btnAddRecipe').textContent = 'REGISTRAR RECETA';
			document.getElementById('h2').textContent = 'REGISTRAR RECETA';
		}
	}

	const regexMyAccount = /^\/facefood\/([^/]+)\/myaccount\/?$/;
	const matchMyAccount = path.match(regexMyAccount);
	if (matchMyAccount) {

		fillUserData(usuarioSession);
		if (document.getElementById('btnAddRecipe')) {
			document.getElementById('btnAddRecipe').textContent = 'ACTUALIZAR PERFIL';
			document.getElementById('h2').textContent = 'ACTUALIZAR PERFIL';
		}
	}

	// Borrar Usuario
	if (document.getElementById("btnDelete")) {
		document.getElementById("btnDelete").addEventListener("click", function (e) {
			e.preventDefault()
			Swal.fire({
				title: '¿Eliminar cuenta?',
				text: "Introduce tu contraseña para confirmar.",
				icon: 'warning',
				input: 'password',
				inputPlaceholder: 'Contraseña',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Sí, eliminar',
				cancelButtonText: 'Cancelar',
				inputValidator: (value) => {
					if (!value) {
						return 'Debes introducir tu contraseña';
					}
				}
			}).then(async (result) => {
				if (result.isConfirmed) {
					const password = result.value;
					// Validar con backend antes de borrar
					const usuario = getUser();
					const isValid = await verifyUser(usuario.usuario, password);
					if (isValid) {
						apiDeleteUsuario(usuario.id);
					} else {
						errorSwal("Error", "Contraseña incorrecta");
					}
				}
			});
		})
	}

	let btnResetPass = document.getElementById("btnPassword")
	if (btnResetPass) {
		btnResetPass.addEventListener("click", async function (e) {
			e.preventDefault();
			let errors = [];

			const password = document.getElementById("password");
			const passwordConfirm = document.getElementById("password2");

			if (password.value != "") {
				if (password.value.length < 6) {
					errors.push({ field: password, message: "La contraseña debe tener al menos 6 caracteres" })
				}
				if (password.value !== passwordConfirm.value) {
					errors.push({ field: passwordConfirm, message: "Las contraseñas no coinciden" })
				}
			}

			if (errors.length > 0) {
				mostrarErrores(errors)
			} else {
				const token = new URLSearchParams(window.location.search).get("token");
				apiSavePassword(password.value, token)
			}
		})
	}

	if (document.getElementById("search-mobile")) {
		document.getElementById("search-mobile").addEventListener("click", async function (e) {
			document.getElementById('sidebar').classList.toggle('open');
			document.getElementById('overlay').classList.toggle('open');
			document.getElementById('search-mobile').classList.toggle('open');
		})
	}
	if (document.getElementById('overlay')) document.getElementById('overlay').addEventListener('click', cerrarSidebar);

	function abrirSidebar() {
		document.getElementById('sidebar').classList.add('open');
		document.getElementById('overlay').classList.add('open');
	}

	function cerrarSidebar() {
		document.getElementById('sidebar').classList.remove('open');
		document.getElementById('overlay').classList.remove('open');
		document.getElementById('search-mobile').classList.remove('open');

	}
});





