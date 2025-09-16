// demoApi.js  (colócalo en /static/js/ o /src/services/)
const DEMO = true; // o lee ?demo=1 desde la URL
const MOCK_BASE = "../static/mock";
const IMG_BASE  = "../static/img/recetas";
const PLACEHOLDER = `${IMG_BASE}/default.png`; 

async function fetchJSON(path) {
  const res = await fetch(path);
  if (!res.ok) throw new Error(`No se pudo cargar ${path}`);
  return res.json();
}

export async function listRecipes() {
  if (DEMO) return fetchJSON(`${MOCK_BASE}/recipes.json`);
  return fetch('/facefood/api/recipe/findAll').then(r => r.json());
}

export async function getRecipeById(id) {
  if (DEMO) {
    const res = await fetch(`./mock/recipe-${id}.json`);
    return res.json();
  }
  return fetch(`/facefood/api/recipe/find/${id}`).then(r => r.json());
}

export async function listUsers() {
  if (DEMO) {
    const res = await fetch('./mock/users.json');
    return res.json();
  }
  return fetch('/facefood/api/user/findAll').then(r => r.json());
}

// Ordena por mejor puntuación (y en empate, por nº de valoraciones)
export function sortByBest(a, b) {
  const score = x => (Number(x.averageRating || 0) * 100) + Number(x.ratingsCount || 0);
  return score(b) - score(a);
}

// Baraja y coge N
export function sample(arr, n) {
  const a = arr.slice();
  for (let i = a.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [a[i], a[j]] = [a[j], a[i]];
  }
  return a.slice(0, n);
}