function editPost(buttonElement) {
    console.log(buttonElement)
    var postId = buttonElement.getAttribute('post-id');
    var postAuthor = buttonElement.getAttribute('post-author');


    fetch('/is-editable', {
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
            window.location.href = "/edit?postId=" + postId;
        } else if (text === "failed") {
            window.alert('Unauthorized: You do not have permission to edit this post');
            // Handle unauthorized situation here
        } else {
            console.error('Failed to edit post, server responded with:', text);
            // Handle other server responses
        }
    })
        .catch(error => {
        console.error('Error during fetch operation:', error);
        // Handle fetch errors
    });
}