export function getRecipeUri(method, param1 = "", param2 = "") {
	const routes = {
		findAll: "/facefood/api/recetas/",
		findAllFav: `/facefood/api/recetas/${param1}/fav`,
		findMyRecipes: `/facefood/api/recetas/${param1}/my`,
		findUserFav: `/facefood/api/recetas/${param1}/myfav`,
		findByCountry: `/facefood/api/recetas/${param1}/country`,
		findByWords: `/facefood/api/recetas/text/${param1}`,
		findById: `/facefood/api/recetas/receta/${param1}`,
		findByIdAndUser: `/facefood/api/recetas/${param1}/recipe/${param2}`,
		findBestRecetas: `/facefood/api/recetas/best`,
		findNewRecetas: `/facefood/api/recetas/new`,
		findOldRecetas: `/facefood/api/recetas/old`,
		updateRecipe: `/facefood/api/recetas/${param1}/receta/${param2}/update`,
		add: `/facefood/api/recetas/${param1}/add`,
		deleteRecipe: `/facefood/api/recetas/${param1}/delete`,
		findBestRecetasByWordsAndCountry: `/facefood/api/recetas/${param1}/text/${param2}/countryBest`,
		findNewRecetasByWordsAndCountry: `/facefood/api/recetas/${param1}/text/${param2}/countryNew`,
		findOldRecetasByWordsAndCountry: `/facefood/api/recetas/${param1}/text/${param2}/countryOld`,
		findBestRecetasByWords: `/facefood/api/recetas/${param1}/text/best`,
		findNewRecetasByWords: `/facefood/api/recetas/${param1}/text/new`,
		findOldRecetasByWords: `/facefood/api/recetas/${param1}/text/old`,
		findBestRecetasByCountry: `/facefood/api/recetas/${param1}/Countrybest`,
		findNewRecetasByCountry: `/facefood/api/recetas/${param1}/CountryNew`,
		findOldRecetasByCountry: `/facefood/api/recetas/${param1}/CountryOld`,
		findByWordsAndCountry: `/facefood/api/recetas/${param1}/text/${param2}/country`,
		findRandom: "/facefood/api/recetas/random",
	};

	return routes[method] || "";
}

export function getUserUri(method, param1 = "", param2 = "") {
	const routes = {
		verifyUser: `/facefood/api/usuarios/${param1}/${param2}`,
		findByUserName: `/facefood/api/usuarios/${param1}`,
		deleteRecipe: `/facefood/api/usuarios/${param1}/delete`,
		updateUser: `/facefood/api/usuarios/${param1}/update`,
		requestPassword: `/facefood/api/usuarios/requestPassword`,
		resetPassword: `/facefood/api/usuarios/resetPassword`,
		findByEmail: `/facefood/api/usuarios/${param1}/email`
	};

	return routes[method] || "";
}

export function getFavoritosUri(method, param1 = "", param2 = "") {
	const routes = {
		delete: `/facefood/api/favoritos/delete/${param1}/${param2}`,
		add: `/facefood/api/favoritos/add/${param1}/${param2}`,
	};

	return routes[method] || "";
}

export function getIngredienteUri(method, param1 = "", param2 = "") {
	const routes = {
		findAllByIdRecipe: `/facefood/api/ingrediente/${param1}`,
		findAll: `/facefood/api/ingrediente/`
	};

	return routes[method] || "";
}

export function getPasoUri(method, param1 = "", param2 = "") {
	const routes = {
		findAllByIdRecipe: `/facefood/api/paso/${param1}`,
	};

	return routes[method] || "";
}

export function getValoracionUri(method, param1 = "", param2 = "") {
	const routes = {
		findRating: `/facefood/api/valoracion/${param1}/${param2}/rate`,
	};

	return routes[method] || "";
}