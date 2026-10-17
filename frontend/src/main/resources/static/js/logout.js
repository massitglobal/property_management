const TOKEN_KEY = 'jwt_token';
const REFRESH_KEY = 'jwt_refresh_token';

function signOut(){
    //iterate the array of session storage in browser
    [localStorage, sessionStorage].forEach(store => {
        store.removeItem(TOKEN_KEY);
        store.removeItem(REFRESH_KEY);

    });

    //redirect the page back to login
    window.location.replace('login.html');
}