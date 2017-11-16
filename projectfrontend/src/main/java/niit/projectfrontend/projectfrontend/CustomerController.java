package niit.projectfrontend.projectfrontend;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import niit.projectbackend.projectbackend.Cart;
import niit.projectbackend.projectbackend.CartItems;
import niit.projectbackend.projectbackend.Customer;
import niit.projectbackend.projectbackend.Product;
import niit.projectbackend.projectbackend.dao.CartDao;
import niit.projectbackend.projectbackend.dao.CartItemsDao;
import niit.projectbackend.projectbackend.dao.CustomerDao;
import niit.projectbackend.projectbackend.dao.ProductDao;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired	CustomerDao customerDao;
	@Autowired ProductDao productDao;
	@Autowired CartItemsDao cartItemsDao;
	@Autowired CartDao cartDao;

	@RequestMapping("/addToCart/{productId}")
	public String addToCart(@PathVariable("productId") Integer proId, Principal principal, Model m) {
		
		Product product=productDao.getProduct(proId);
		
		Customer customer = customerDao.getCustomer(principal.getName());

		Cart cart = customer.getCart();
		getCartItems(cart.getCartId(),product.getProductId());
		CartItems cartItems=new CartItems();
		
		cartItems.setProduct(product);
		
		cartItems.setCartItemsQuantity(1);
		
		cartItems.setCartItemsPrice(product.getProductPrice());
		cartItems.setCart(cart);
		ArrayList<CartItems> items=new  ArrayList<CartItems>();
		items.add(cartItems);
		cart.setCartItems(items);
		if(cart.getCartQuantity()==null)
		{
		cart.setCartQuantity(1);
		}else
		{
			cart.setCartQuantity(cart.getCartQuantity()+1);
		}
		if(cart.getCartTotalPrice()==null)
		{
		cart.setCartTotalPrice(product.getProductPrice());
		}else
		{
			cart.setCartTotalPrice(cart.getCartTotalPrice()+product.getProductPrice());
		}
		
		cartItemsDao.addCartItems(cartItems);
		cartDao.updateCart(cart);
		
		return "redirect:/";

	}
}
