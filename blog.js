// Blog post submission
document.addEventListener('DOMContentLoaded', function() {
    const blogForm = document.getElementById('blog-form');
    const blogGrid = document.querySelector('.blog-grid');
    const imageInput = document.getElementById('blog-image');
    const imagePreview = document.getElementById('image-preview');

    // Image preview functionality
    imageInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.innerHTML = `<img src="${e.target.result}" alt="Preview">`;
            }
            reader.readAsDataURL(file);
        }
    });

    blogForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const title = document.getElementById('blog-title').value;
        const author = document.getElementById('author-name').value;
        const content = document.getElementById('blog-content').value;
        const imageFile = imageInput.files[0];

        // Create new blog post element
        const newPost = document.createElement('article');
        newPost.className = 'blog-post';

        if (imageFile) {
            const reader = new FileReader();
            reader.onload = function(e) {
                newPost.innerHTML = `
                    <img src="${e.target.result}" alt="${title}">
                    <h2>${title}</h2>
                    <p class="post-meta">By ${author} | ${new Date().toLocaleDateString()}</p>
                    <p>${content.substring(0, 150)}...</p>
                    <a href="#" class="btn read-more">Read More</a>
                `;
                // Add new post to the grid
                blogGrid.insertBefore(newPost, blogGrid.firstChild);
            }
            reader.readAsDataURL(imageFile);
        } else {
            newPost.innerHTML = `
                <h2>${title}</h2>
                <p class="post-meta">By ${author} | ${new Date().toLocaleDateString()}</p>
                <p>${content.substring(0, 150)}...</p>
                <a href="#" class="btn read-more">Read More</a>
            `;
            // Add new post to the grid
            blogGrid.insertBefore(newPost, blogGrid.firstChild);
        }

        // Clear form fields
        blogForm.reset();
        imagePreview.innerHTML = '';

        // Show success message
        alert('Blog post submitted successfully!');
    });
});

