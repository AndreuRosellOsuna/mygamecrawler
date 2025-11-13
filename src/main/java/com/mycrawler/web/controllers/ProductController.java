package com.mycrawler.web.controllers;

import com.mycrawler.web.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@PreAuthorize("hasAuthority('ROLE_USER')")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/products")
    public String getProducts(@AuthenticationPrincipal UserDetails user, Model model) {
        var productList = productService.getProducts(user.getUsername());
        model.addAttribute("productList", productList);
        return "products";
    }

    @RequestMapping(value = "/subscribe-product", method = RequestMethod.POST)
    public String subscribe(@AuthenticationPrincipal UserDetails user, @ModelAttribute("productId") Long productId) {
        productService.subscribeToProduct(user.getUsername(), productId);
        return "redirect:/products";
    }

    @RequestMapping(value = "/unsubscribe-product", method = RequestMethod.POST)
    public String unsubscribe(@AuthenticationPrincipal UserDetails user, @ModelAttribute("productId") Long productId) {
        productService.unsubscribeToProduct(user.getUsername(), productId);
        return "redirect:/products";
    }
}
