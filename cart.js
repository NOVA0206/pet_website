let cart = JSON.parse(localStorage.getItem('cart')) || [];

const cartOverlay = document.getElementById('cart-overlay');
const cartItems = document.getElementById('cart-items');
const cartSubtotal = document.getElementById('cart-subtotal');
const cartTax = document.getElementById('cart-tax');
const cartTotal = document.getElementById('cart-total');
const cartCount = document.getElementById('cart-count');
const cartIcon = document.getElementById('cart-icon');
const viewCartBtn = document.getElementById('view-cart-btn');
const checkoutBtn = document.getElementById('checkout-btn');

function updateCartDisplay() {
    cartItems.innerHTML = '';
    let subtotal = 0;

    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        subtotal += itemTotal;

        cartItems.innerHTML += `
            <div class="cart-item">
                <img src="${item.image}" alt="${item.name}">
                <div class="item-details">
                    <div class="item-name">${item.name}</div>
                    <div class="item-price">Rs. ${item.price}</div>
                    <div class="item-quantity">
                        <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity - 1})">-</button>
                        <input type="number" value="${item.quantity}" min="1" onchange="updateQuantity(${item.id}, this.value)">
                        <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
                    </div>
                </div>
                <span class="remove-item" onclick="removeItem(${item.id})">×</span>
            </div>
        `;
    });

    const tax = subtotal * 0.05;
    const total = subtotal + tax;

    cartSubtotal.textContent = `Rs. ${subtotal.toFixed(2)}`;
    cartTax.textContent = `Rs. ${tax.toFixed(2)}`;
    cartTotal.textContent = `Rs. ${total.toFixed(2)}`;
    cartCount.textContent = cart.reduce((sum, item) => sum + item.quantity, 0);

    localStorage.setItem('cart', JSON.stringify(cart));
}

function addToCart(id, name, price, image) {
    const existingItem = cart.find(item => item.id === id);
    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.push({ id, name, price, image, quantity: 1 });
    }
    updateCartDisplay();
    cartOverlay.classList.add('active');
}

function updateQuantity(id, newQuantity) {
    const index = cart.findIndex(item => item.id === id);
    if (index !== -1) {
        newQuantity = parseInt(newQuantity);
        if (newQuantity > 0) {
            cart[index].quantity = newQuantity;
        } else {
            cart.splice(index, 1);
        }
        updateCartDisplay();
    }
}

function removeItem(id) {
    cart = cart.filter(item => item.id !== id);
    updateCartDisplay();
}

cartIcon.addEventListener('click', (e) => {
    e.preventDefault();
    cartOverlay.classList.toggle('active');
});

viewCartBtn.addEventListener('click', () => {
    window.location.href = 'cart.html';
});

checkoutBtn.addEventListener('click', () => {
    alert('Thank you for your purchase! This is where you would integrate a payment gateway.');
});

document.addEventListener('click', (e) => {
    if (!cartOverlay.contains(e.target) && e.target !== cartIcon) {
        cartOverlay.classList.remove('active');
    }
});

// Initialize the cart display
updateCartDisplay();