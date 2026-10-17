// ---- Config (placeholders: replace with your backend values) ----
const API_LOGIN_URL = 'http://localhost:8080/auth/login';   // TODO: your login endpoint
const REDIRECT_AFTER_LOGIN = 'dashboard.html'; // TODO: page to open after login
const TOKEN_KEY = 'jwt_token';
const REFRESH_KEY = 'jwt_refresh_token';

const form = document.getElementById('loginForm');
const alertBox = document.getElementById('alert');
const spinner = document.getElementById('spinner');
const submitBtn = document.getElementById('submitBtn');

// Show / hide password
document.getElementById('togglePassword').addEventListener('click', (e) => {
    const input = document.getElementById('password');
    const show = input.type === 'password';
    input.type = show ? 'text' : 'password';
    e.currentTarget.querySelector('i').className = show ? 'bi bi-eye-slash' : 'bi bi-eye';
    e.currentTarget.setAttribute('aria-label', show ? 'Hide password' : 'Show password');
});

// Already logged in? Skip the form.
if (getToken()) window.location.replace(REDIRECT_AFTER_LOGIN);

form.addEventListener('submit', async (e) => {
    e.preventDefault();
    alertBox.classList.add('d-none');

    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    setLoading(true);
    try {
        const res = await fetch(API_LOGIN_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: document.getElementById('username').value.trim(),
                password: document.getElementById('password').value
            })
        });

        const body = await res.json();

        if (!res.ok || body.responseCode !== '00') {
            throw new Error(body.responseMsg || 'Sign in failed, please try again');
        }

        const {accessToken, refreshToken} = body.data || {};
        if (!accessToken) throw new Error('No token returned by the server.');

        saveToken(accessToken, refreshToken, document.getElementById('remember').checked);
        window.location.href = REDIRECT_AFTER_LOGIN;
    } catch (err) {
        alertBox.textContent = err.message;
        alertBox.classList.remove('d-none');
    } finally {
        setLoading(false);
    }
});

function setLoading(on) {
    spinner.classList.toggle('d-none', !on);
    submitBtn.disabled = on;
}

// "Remember me" -> localStorage (persists); otherwise sessionStorage (clears on tab close).
// For stronger security, have the server set the JWT in an HttpOnly, Secure cookie instead.
function saveToken(access, refresh, remember) {
    const store = remember ? localStorage : sessionStorage;
    store.setItem(TOKEN_KEY, access);
    if (refresh)
        store.setItem(REFRESH_KEY, refresh);
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY) || sessionStorage.getItem(TOKEN_KEY);
}

// On protected pages, send the token with each request:
//   fetch('/api/secure', { headers: { Authorization: 'Bearer ' + getToken() } })

