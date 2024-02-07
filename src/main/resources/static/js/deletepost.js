function deletePost(buttonElement) {
    console.log(buttonElement)
    var postId = buttonElement.getAttribute('post-id');
    var postAuthor = buttonElement.getAttribute('post-author');
    fetch('/delete-post', {
        method: 'POST', // or 'DELETE', depending on your server setup
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(
        {
            id: postId,
            author: postAuthor
        }
        ),
    })
        .then(response => response.text())  // Convert the response to text
        .then(text => {
        if (text === "success") {
            window.alert('You deleted this post');
            // Remove the post element from the DOM
            location.reload();
        } else if (text === "failed") {
            window.alert('Unauthorized: You do not have permission to delete this post');
            // Handle unauthorized situation here
        } else {
            console.error('Failed to delete post, server responded with:', text);
            // Handle other server responses
        }
    })
        .catch(error => {
        console.error('Error during fetch operation:', error);
        // Handle fetch errors
    });
}
