function deletePost(postId) {
    // Replace 'postId' with the actual ID of the post
    fetch('/delete-post', {
        method: 'POST', // or 'DELETE', depending on your server setup
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ id: postId }),
    })
        .then(response => {
        if (response.ok) {
            // Remove the post element from the DOM
            document.getElementById('post-' + postId).remove();
        } else {
            // Handle errors
            console.error('Failed to delete post');
        }
    });
}
