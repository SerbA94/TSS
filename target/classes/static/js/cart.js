if (document.readyState == 'loading') {
	document.addEventListener('DOMContentLoaded', ready)
} else {
	ready()
}

function ready() {

	var removeCartItemButtons = document.getElementsByClassName('btn-remove')
	for (var i = 0; i < removeCartItemButtons.length; i++) {
		var button = removeCartItemButtons[i]
		button.addEventListener('click', removeCartItem )
	}
	 
	var quantityInputs = document.getElementsByClassName('product-quantity-input')
	for (var i = 0; i < quantityInputs.length; i++) {
		var input = quantityInputs[i]
	    input.addEventListener('change', quantityChanged)
	}
	
	var addToCartButtons = document.getElementsByClassName('btn-add')
	for (var i = 0; i < addToCartButtons.length; i++) {
		var button = addToCartButtons[i]
		button.addEventListener('click', addToCartClicked)
	}

    document.getElementsByClassName('btn-purchase')[0].addEventListener('click', purchaseClicked)	
}

function purchaseClicked() {
    alert('Thank you for your purchase')
    var cartItems = document.getElementsByClassName('cart-items')[0]
    while (cartItems.hasChildNodes()) {
        cartItems.removeChild(cartItems.firstChild)
    }
    updateCartTotal()
}

function addToCartClicked(event){
	var button = event.target
	var productItem = button.parentElement.parentElement.parentElement
	var productName = productItem.getElementsByClassName('product-name')[0].innerText
	var productPrice = productItem.getElementsByClassName('product-price')[0].innerText
	var productImage = productItem.getElementsByClassName('img-table')[0].src
	addProductToCart(productName,productPrice,productImage)
    updateCartTotal()
}

function addProductToCart(productName,productPrice,productImage) {
	var cartRow = document.createElement('div')
	cartRow.classList.add('cart-row')
	var cartItems = document.getElementsByClassName('cart-items')[0]
	var productNames = cartItems.getElementsByClassName('product-name')
    for (var i = 0; i < productNames.length; i++) {
        if (productNames[i].innerText == productName) {
            alert('This item is already added to the cart')
            return
        }
    }
	
	var cartRowContents = `
		<div class="box fd-row jc-sa">
		    <div>
		    	<img src="${productImage}" class="img-table">
		    	<span class="product-name">${productName}</span>
		    	<input type="hidden" name="nameArray" value="${productName}">
		    </div>
           	<span class="product-price">${productPrice}</span>
            <div>
            	<input class="product-quantity-input" type="number" name="quantityArray" value="1">
            	<button type="submit" class="btn-remove btn">Remove</button>
            </div>
        </div>`
	cartRow.innerHTML = cartRowContents
	cartItems.append(cartRow)
	cartRow.getElementsByClassName('btn-remove')[0].addEventListener('click', removeCartItem)
	cartRow.getElementsByClassName('product-quantity-input')[0].addEventListener('change', quantityChanged)	
}



function removeCartItem(event) {
    var buttonClicked = event.target
    buttonClicked.parentElement.parentElement.parentElement.remove()
    updateCartTotal()
}

function updateCartTotal() {
    var cartItemContainer = document.getElementsByClassName('cart-items')[0]
    var cartRows = cartItemContainer.getElementsByClassName('cart-row')
    var total = 0
    for (var i = 0; i < cartRows.length; i++) {
        var cartRow = cartRows[i]
        var priceElement = cartRow.getElementsByClassName('product-price')[0]
        var quantityElement = cartRow.getElementsByClassName('product-quantity-input')[0]
        var price = parseFloat(priceElement.innerText.replace('$', ''))
        var quantity = quantityElement.value
        total = total + (price * quantity)
    }
    total = Math.round(total * 100) / 100
    document.getElementsByClassName('cart-total-price')[0].innerText = '$' + total
}

function quantityChanged(event) {
    var input = event.target
    if (isNaN(input.value) || input.value <= 0) {
        input.value = 1
    }
    updateCartTotal()
}

