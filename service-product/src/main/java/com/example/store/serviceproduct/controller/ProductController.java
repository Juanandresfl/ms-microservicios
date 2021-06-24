package com.example.store.serviceproduct.controller;

import com.example.store.serviceproduct.entity.Category;
import com.example.store.serviceproduct.entity.Product;
import com.example.store.serviceproduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<?> listarProductos(@RequestParam(name="categoryId", required=false) Long categoryId) {
        List<Product> products = productService.listAllProduct();
        if(categoryId == null) {
            products = productService.listAllProduct();
            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }
        else{
            products = productService.findByCategory(Category.builder().id(categoryId).build());
            if(products.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProducto(@PathVariable Long id){
        Product product = productService.getProduct(id);
        if(product == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping("")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Product product, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        Product producto = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> ModificarProducto(@PathVariable Long id , @RequestBody Product product){
        product.setId(id);
        Product productDb = productService.updateProduct(product);
        if(productDb == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDb);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id){
        Product productD = productService.deleteProduct(id);
        if(productD == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productD);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> updateStockProduct(Long id, @RequestParam(name = "quantity", required = true) Double quantity){
        Product product = productService.updateStock(id,quantity);
        if(product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
