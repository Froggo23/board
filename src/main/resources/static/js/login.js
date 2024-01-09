async function gotoRegi() {
    window.location.href = "/register";
}

async function checkLogin() {
    // Input values
    const u = document.getElementById('username').value;
    const p = document.getElementById('password').value;

    let response;
    try {
        // Send the POST request
        response = await fetch('/checkLogin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username:u,
                password:p,
            })
        });



    } catch (error) {
        alert('Network error. Please check your connection and try again.');
    }
    const responseBody = await response.text();

    if (response.ok && responseBody === "success") {
        document.cookie = `login_id=${u}; expires=${new Date(Date.now() + 3 * 60 * 1000).toUTCString()}; path=/`;
        alert('Login succeeded!');
        window.location.href = "/board";
    } else {
        alert('Login error. Please check your username and password again.');
    }

};
