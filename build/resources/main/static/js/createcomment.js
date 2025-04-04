async function submitComment() {
    const content = document.getElementById('comment').value;
    const postId = window.location.pathname.split('/').pop(); // Extracts the post ID from the URL

    if(!content){
        alert("fill in the comment")
        return
    }

    // Prepare the data to send
    const comment = {
        postId: postId,
        content: content
    };

    try {
        // Send the POST request
        const response = await fetch('/submitComment', { // Ensure the endpoint matches your server configuration
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(comment)
        });

        const responseBody = await response.text();

        if (response.ok && responseBody === "success") {
            alert('Comment added successfully!');
            window.location.reload(); // Reload the page to show the new comment
        } else {
            alert('Error adding comment. Please try again.');
        }
    } catch (error) {
        alert('Error adding comment. Please check your connection and try again.');
    }
}
