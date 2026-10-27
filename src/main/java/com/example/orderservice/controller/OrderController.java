package com.example.orderservice.controller;


import com.example.orderservice.model.request.OrderRequest;
import com.example.orderservice.model.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody @Valid OrderRequest request){
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id){
       OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<Page<OrderResponse>>  getAll(@PageableDefault(page = 0,size = 5) Pageable pageable){
       var orderEntityPage = orderService.getAllOrder(pageable);
        return ResponseEntity.ok(orderEntityPage);
    }

    @GetMapping("/allOrder/{userId}")
    public ResponseEntity<Page<OrderResponse>> getAllOrderByUserId(@PathVariable(name = "userId") Long id ,
                                                                   @PageableDefault(size = 7) Pageable pageable){
       var userOrders = orderService.getAllOrderByUserId(id,pageable);
       return ResponseEntity.ok(userOrders);
    }
}
