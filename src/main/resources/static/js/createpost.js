async function submitPost() {
    // Get the button element
    const button = document.querySelector('button');

    // Add the exploding class to start the animation
    button.classList.add('exploding');

    // Get values from the input fields
    const title = document.getElementById('title').value;
    const author = document.getElementById('author').value;
    const content = document.getElementById('content').value;

    // Prepare the data to send
    const data = {
        title: title,
        author: author,
        content: content
    };

    try {
        // Send the POST request
        const response = await fetch('/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const responseBody = await response.text();

        if (response.ok && responseBody === "success") {
            alert('Post submitted successfully!');
            window.location.href = "/board";
        } else {
            alert('Error submitting post. Please try again.');
        }
    } catch (error) {
        alert('Error submitting post. Please check your connection and try again.');
    }


}
