function openEditPost(buttonElement) {
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

async function editPost() {
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    const id = document.getElementById('id').getAttribute('post-id')

    // Prepare the data to send
    const data = {
        title: title,
        content: content,
        id: id
    };


    try {
        // Send the POST request
        const response = await fetch('/submitEdit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const responseBody = await response.text();

        if (response.ok && responseBody === "success") {
            alert('Post edited successfully!');
            window.location.href = "/board";
        } else if (response.ok && responseBody === "needs login") {
            alert('Needs to edit again!');
            window.location.href = "/login";
        }
        else {
            alert('Error editing post. Please try again.');
        }
    } catch (error) {
        alert('Error editing post. Please check your connection and try again.');
        //    }

    }
}