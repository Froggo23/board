async function submitRegi() {

    // Input values
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phoneNumber').value;

    // Validations
    const passwordRegex = /^.{8,}$/; // At least 8 characters
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/; // Korean phone number format
    const userRegex = /^[a-z0-9_-]{3,15}$/;

    if (!userRegex.test(username)){
        alert('Username must be 3 to 16 characters long. Special characters along with uppercase letters the exception of _ and . arent allowed');
        return;
    }

    if (!passwordRegex.test(password)) {
        alert('Password must be at least 8 characters long.');
        return;
    }

    if (password !== confirmPassword) {
        alert('Passwords do not match.');
        return;
    }

    if (!emailRegex.test(email)) {
        alert('Please enter a valid email address.');
        return;
    }

    if (!phoneRegex.test(phone)) {
        alert('Please enter a valid Korean phone number. ex)010-1234-5678');
        return;
    }

    try {
        // Send the POST request
        const response = await fetch('/regiSubmit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username:username,
                password:password,
                email:email,
                phone:phone
            })
        });

        const responseBody = await response.text();

        if (response.ok && responseBody === "success") {
            alert('Post submitted successfully!');
            window.location.href = "/login";
        } else {
            alert('Error registering user. Please try again.');
        }
    } catch (error) {
        alert('Network error. Please check your connection and try again.');
    }


};

let usernameCheckTimeout; // Timeout variable

async function validateUsername() {
    clearTimeout(usernameCheckTimeout); // Clear previous timeout
    usernameCheckTimeout = setTimeout(async () => {
        const u = document.getElementById('username').value;
        try {
            const response = await fetch('/checkDuplicate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username:u,
                })
            });
            const responseBody = await response.text();

            if (response.ok && responseBody === "exists") {
                document.getElementById('usernameStatus').textContent = '❌';
            } else {
                document.getElementById('usernameStatus').textContent = '✅';
            }
        } catch (error) {
            console.error('Error checking username', error);
        }
    }, 3000); // Wait for 3 seconds after typing stops
}

