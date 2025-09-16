import { apiAddUsuario, apiUpdateUsuario, checkAvailability, findAllPaises } from "./api.js";
import { PATH_IMG_AVATAR } from "./const.js";
import { revisarYCortarImagenCuadrada } from "./photo.js";
import { fillDisableInputForm, limpiarErrores, mostrarErrores, validateEmail, validateFile, validateInput, validateSelect, validateUsername } from "./ui.js";

export function getUser() {
	if (sessionStorage.getItem("usuario")) {
		return JSON.parse(sessionStorage.getItem("usuario"));
	}
	return sessionStorage.getItem("usuario")
}

export function saveUser(user) {
	sessionStorage.setItem("usuario", JSON.stringify(user));
}

export function setUserOut() {
	sessionStorage.setItem("usuario", null);
}

export function userIsLogged(usuarioSession) {
	if (usuarioSession && typeof usuarioSession === "object" && usuarioSession.id) {
		return true;
	}
	return false;
}

export async function fillUserData(usuarioSession) {
	if (!userIsLogged(usuarioSession)) {
		window.location.href = "/facefood/login";
		return;
	}
	document.getElementById("title").innerHTML = "Bienvenid@ de nuevo " + usuarioSession.nombre + " " + usuarioSession.apellidos;
	fillDisableInputForm("name", usuarioSession.nombre)
	fillDisableInputForm("lastName", usuarioSession.apellidos)
	fillDisableInputForm("userName", usuarioSession.usuario)
	fillDisableInputForm("email", usuarioSession.email)
	fillDisableInputForm("presentacion", usuarioSession.presentacion)
	if (usuarioSession.presentacion == "") document.getElementById('presentacion').placeholder = ""

	document.getElementById("main-image").src = `${usuarioSession.avatar}`;
	document.getElementById("main-image").alt = usuarioSession.avatar;

	let countrySelect = document.getElementById('country');
	await findAllPaises(countrySelect)
	countrySelect.value = usuarioSession.paisDTO.id;
	countrySelect.setAttribute("disabled", "disabled")
	document.getElementById('password').placeholder = "*******";
	document.getElementById('password').setAttribute("disabled", "disabled");
	document.getElementById('password2').placeholder = "*******";
	document.getElementById('password2').setAttribute("disabled", "disabled");
	document.getElementById('main-image-file').setAttribute("disabled", "disabled");

	document.getElementById("btnDelete").setAttribute("style", "display: block;");

	document.getElementById('back-home').addEventListener('click', function (e) {
		e.preventDefault();
		window.location.href = `/facefood/${usuarioSession.usuario}`;
	});

	document.getElementById("btnRegister").textContent = 'EDITAR';
	document.getElementById("btnRegister").addEventListener('click', function (e) {
		e.preventDefault();
		if (document.getElementById("btnRegister").textContent === 'EDITAR') {
			habilitarCampos(usuarioSession);
		} else {
			validateYApiUsuarioForm(usuarioSession);
		}
	})

}

function habilitarCampos(usuarioSession) {
	document.getElementById("name").removeAttribute("disabled");
	document.getElementById("lastName").removeAttribute("disabled");
	document.getElementById("userName").removeAttribute("disabled");
	document.getElementById("email").removeAttribute("disabled");
	document.getElementById('country').removeAttribute("disabled");
	document.getElementById('password').removeAttribute("disabled");
	document.getElementById('password2').removeAttribute("disabled");
	document.getElementById('presentacion').removeAttribute("disabled");
	document.getElementById('main-image-file').removeAttribute("disabled");
	if (usuarioSession.presentacion == "") document.getElementById('presentacion').placeholder = "Descríbete en una frase"

	document.getElementById("btnRegister").textContent = 'ACTUALIZAR';
}

export async function validateYApiUsuarioForm(usuarioSession) {
	limpiarErrores()
	let errors = [];

	const nombre = document.getElementById("name");
	const apellidos = document.getElementById("lastName");
	const email = document.getElementById("email");
	const usuario = document.getElementById("userName");
	const country = document.getElementById("country");
	const password = document.getElementById("password");
	const passwordConfirm = document.getElementById("password2");
	const presentacion = document.getElementById("presentacion");
	const mainImg = document.getElementById("main-image-file");

	validateInput(nombre, errors, "Introduce el nombre")
	validateInput(apellidos, errors, "Introduce los apellidos")
	validateUsername(usuario, errors)
	validateEmail(email, errors, "Introduce un email válido")
	validateSelect(country, errors, "Selecciona un país")
	//validateInput(presentacion, errors, "Introduce una presentación")

	if (userIsLogged(usuarioSession)) {
		if (usuarioSession.usuario !== usuario.value) {
			const isAvailable = await checkAvailability("findByUserName", usuario.value);
			if (!isAvailable) {
				errors.push({ field: usuario, message: "El nombre de usuario ya está en uso" });
			}
		}

		if (usuarioSession.email !== email.value) {
			const emailExists = await checkAvailability("findByEmail", email.value)
			if (!emailExists) {
				errors.push({ field: email, message: "El email ya está siendo usado" });
			}
		}

		if (password.value != "") {
			if (password.value.length < 6) {
				errors.push({ field: password, message: "La contraseña debe tener al menos 6 caracteres" })
			}
			if (password.value !== passwordConfirm.value) {
				errors.push({ field: passwordConfirm, message: "Las contraseñas no coinciden" })
			}
		}


	} else {
		validateFile(mainImg, document.getElementById("src-file1"), errors, "Selecciona una imagen")

		const isAvailable = await checkAvailability("findByUserName", usuario.value);
		if (!isAvailable) {
			errors.push({ field: usuario, message: "El nombre de usuario ya está en uso" });
		}

		const emailExists = await checkAvailability("findByEmail", email.value)
		if (!emailExists) {
			errors.push({ field: email, message: "El email ya está siendo usado" });
		}

		if (validateInput(password, errors, "Introduce una contraseña")) {
			if (validateInput(passwordConfirm, errors, "Repite la contraseña")) {
				if (password.value.length < 6) {
					errors.push({ field: password, message: "La contraseña debe tener al menos 6 caracteres" })
				}
				if (password.value !== passwordConfirm.value) {
					errors.push({ field: passwordConfirm, message: "Las contraseñas no coinciden" })
				}
			}
		}
	}

	let mainImgRecortada = null

	if (mainImg.files.length > 0) {
		mainImgRecortada = await revisarYCortarImagenCuadrada(
			mainImg,
			document.getElementById("src-file1"),
			errors,
			"Selecciona una imagen principal"
		);
	}

	if (errors.length > 0) {
		mostrarErrores(errors)
	} else {
		let btnRegister = document.getElementById("btnRegister")
		btnRegister.disabled = true;
		btnRegister.textContent = "CARGANDO...";
		document.getElementById("loading-msg").style.display = "inline";
		if (!userIsLogged(usuarioSession)) apiAddUsuario(nombre, apellidos, email, usuario, country, password, presentacion, mainImgRecortada)
		else apiUpdateUsuario(usuarioSession, nombre, apellidos, email, usuario, country, password, presentacion, mainImgRecortada)
	}

}