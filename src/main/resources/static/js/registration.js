document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // Input values
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;

    // Validations
    const passwordRegex = /^.{8,}$/; // At least 8 characters
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/; // Korean phone number format

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
        alert('Please enter a valid Korean phone number.');
        return;
    }

    // Submit the form or send data to server here
    console.log('Registration Successful!');
    alert('Registration Successful!'); // Placeholder for actual registration logic
});
