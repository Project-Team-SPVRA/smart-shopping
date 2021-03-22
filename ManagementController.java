package net.spvra.smartshopping.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.spvra.shoppingbackend.dao.CategoryDAO;
import net.spvra.shoppingbackend.dao.ProductDAO;
import net.spvra.shoppingbackend.dto.Category;
import net.spvra.shoppingbackend.dto.Product;

@Controller
@RequestMapping("/manage")
public class ManagementController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	
	private static final Logger logger =  LoggerFactory.getLogger(ManagementController.class);
	
	
	@RequestMapping(value= "/product", method=RequestMethod.GET) //for displaying the data in the form
	public ModelAndView showManageProducts(@RequestParam(name="operation" , required=false) String operation) {
		
		

		ModelAndView mv = new ModelAndView("page");	
		mv.addObject("title","Product Management");		
		mv.addObject("userClickManageProduct",true);
		
		Product nProduct = new Product();
		nProduct.setSupplierId(1); //1 is for admin 
		nProduct.setActive(true); //assuming the admin entered the product is active
		
		mv.addObject("product", nProduct); //adding the new product here
		
		if(operation != null)
		{
			if(operation.equals("product"))
			{
				mv.addObject("message", "Product details submitted successfully");
			}
		}
		
		
		
		return mv;
	}

	
	
	//returning categories for all the request mapping
	  @ModelAttribute("categories")
	  public List<Category> getCategories() {
		  
		  return categoryDAO.list();
	  
	  }
	  
	  //handling product submission
	  @RequestMapping(value="/product", method=RequestMethod.POST) //called when submit button is clicked
	  public String handleProductSubmission(@Valid  @ModelAttribute("product") Product mProduct , 
			                             BindingResult results, Model model) //here model is used to pass any data to the view
	  
	  	{
		  
		  //checking if there are any errors
		  if(results.hasErrors())
		  {
			 
			  model.addAttribute("userClickManageProduct",true);
			  model.addAttribute("title","Product Management");
			  model.addAttribute("message", "validation failed for product submission!");
			  
			  
			  return "page"; //we have not used redirect here because then the error wont get displayed
		  }
		  
		  
		  logger.info(mProduct.toString());
		  
		  //create a new product record
		  productDAO.add(mProduct);
		  
		  
		  return "redirect:/manage/product?operation=product"; //here redirect because we needed to go to a new url
		 
	  }
	 
}