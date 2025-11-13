package com.mycrawler.web.controllers;

import com.mycrawler.web.service.SubscriptionService;
import com.mycrawler.web.views.SubscriptionView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ROLE_USER')")
public class SubscriptionsController {

    private final SubscriptionService subscriptionService;

    public SubscriptionsController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping("/subscriptions")
    public String getSubscriptions(@AuthenticationPrincipal UserDetails user, Model model) {
        List<SubscriptionView> subscriptions = subscriptionService.getSubscriptions(user.getUsername());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("subscriptions", subscriptions);
        return "subscriptions";
    }
}
