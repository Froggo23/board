function deleteComment(buttonElement) {
    var commentId = buttonElement.getAttribute('comment-id');
    var author = buttonElement.getAttribute('author');
    fetch('/delete-comment', {
        method: 'POST', // or 'DELETE', depending on your server setup
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(
            {
                id: commentId,
                author: author
            }
        ),
    })
        .then(response => response.text())  // Convert the response to text
        .then(text => {
        if (text === "success") {
            window.alert('You deleted this comment');
            // Remove the post element from the DOM
            location.reload();
        } else if (text === "failed") {
            window.alert('Unauthorized: You do not have permission to delete this comment');
            // Handle unauthorized situation here
        } else {
            console.error('Failed to delete comment, server responded with:', text);
            // Handle other server responses
        }
    })
        .catch(error => {
        console.error('Error during fetch operation:', error);
        // Handle fetch errors
    });
}