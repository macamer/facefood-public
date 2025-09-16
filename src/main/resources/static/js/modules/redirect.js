import { getRecipe } from "./recipe.js"
import { getUser, userIsLogged } from "./user.js"

let showLogout = document.getElementById("logout")
let showRegister = document.getElementById('register')
let showLogin = document.getElementById('login')
let showRecipeForm = document.getElementById('addrecipe')
let showMain = document.getElementById('back-main')
let showHome = document.getElementById('back-home')
let showRecipeEdit = document.getElementById('editrecipe')

/*if (showLogout != null) {
	document.getElementById("logout").addEventListener("click", function (e) {
		e.preventDefault();
		sessionStorage.setItem("usuario", "")
		window.location.href = '/facefood/';
	})
}*/

if (showRegister != null) {
	showRegister.addEventListener('click', function (e) {
		e.preventDefault();
		window.location.href = '/facefood/add';
	});
}

if (showLogin != null) {
	showLogin.addEventListener('click', function (e) {
		e.preventDefault();
		window.location.href = '/facefood/login';
	});
}

if (showRecipeForm != null) {
	showRecipeForm.addEventListener('click', function (e) {
		e.preventDefault();
		let usuarioSes = getUser()
		if (!userIsLogged(usuarioSes)) {
			Swal.fire({
				title: "Iniciar sesión",
				text: "Para añadir una receta debes iniciar sesión.",
				icon: "warning",
				showCancelButton: true,
				confirmButtonText: "Iniciar sesión",
				cancelButtonText: "Cancelar",
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.href = "/facefood/login";
				}
			});
		} else window.location.href = '/facefood/' + usuarioSes.usuario + '/receta/add';
	});
}

if (showRecipeEdit != null) {
	showRecipeEdit.addEventListener('click', function (e) {
		e.preventDefault();
		let usuarioSes = getUser()
		let recetaSession = getRecipe()
		window.location.href = '/facefood/' + usuarioSes.usuario + '/receta/' + recetaSession.id + '/update';
	});
}

if (showMain != null) {
	showMain.addEventListener('click', function (e) {
		e.preventDefault();
		let usuarioSes = getUser()
		if (!userIsLogged(usuarioSes)) window.location.href = '/facefood/main'
		else 	window.location.href = '/facefood/' + usuarioSes.usuario;
	});
}

if (showHome != null) {
	showHome.addEventListener('click', function (e) {
		e.preventDefault();
		let usuarioSession = getUser()
		if (!userIsLogged(usuarioSession)) {
			window.location.href = '/facefood/';
		}
	});
}
