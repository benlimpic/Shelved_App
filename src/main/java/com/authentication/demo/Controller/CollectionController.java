package com.authentication.demo.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.authentication.demo.Exceptions.CollectionCreationException;
import com.authentication.demo.Model.CollectionModel;
import com.authentication.demo.Repository.CollectionRepository;
import com.authentication.demo.Service.CollectionService;

@Controller
public class CollectionController {

    private final CollectionService collectionService;
    private final CollectionRepository collectionRepository;

    public CollectionController(@Lazy CollectionService collectionService, CollectionRepository collectionRepository) {
        this.collectionService = collectionService;
        this.collectionRepository = collectionRepository;
    }

    // CREATE NEW COLLECTION
    @PostMapping("/create_collection")
    public String postCollection(@RequestParam Map<String, String> collectionDetails,
            @RequestParam MultipartFile collectionImage,
            RedirectAttributes redirectAttributes) throws IOException {

            if (collectionDetails == null) {
                redirectAttributes.addFlashAttribute("error", "Collection cannot be empty");
                return "redirect:/create-collection";
            }
        

            // Validate collection details
            if (collectionDetails.get("title") == null || collectionDetails.get("title").isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Collection title cannot be empty");
                return "redirect:/create-collection";
            }
            if (collectionImage == null || collectionImage.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Collection image cannot be empty");
                return "redirect:/create-collection";
            }
            
            try {
                collectionService.createCollection(collectionDetails, collectionImage);
                redirectAttributes.addFlashAttribute("message", "Collection created successfully");
                return "redirect:/profile";
            } catch (CollectionCreationException | IOException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/create-collection";
            }
    }

    // UPDATE COLLECTION
    @PostMapping("/update-collection/{id}")
    public String updateCollection(
            @PathVariable("id") Long collectionId,
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) MultipartFile collectionImage,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
            params.put("id", String.valueOf(collectionId));
            collectionService.updateCollection(params, collectionImage);
            redirectAttributes.addFlashAttribute("message", "Collection updated successfully");
        } catch (CollectionCreationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/update-collection/" + collectionId;
        }
        redirectAttributes.addFlashAttribute("message", "Collection updated successfully");
        return "redirect:/collection/" + collectionId;
    }

    
    @PostMapping("/delete-collection/{id}")
    public String deleteCollection(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        collectionService.deleteCollection(id);

        redirectAttributes.addFlashAttribute("message", "Collection deleted successfully");
        return "redirect:/profile";
    }


    @GetMapping("/search-collections")
    public ResponseEntity<Map<String, Object>> searchCollections(@RequestParam("query") String query) {
        List<CollectionModel> collections = collectionRepository.findByTitleContainingIgnoreCase(query);
        Map<String, Object> response = new HashMap<>();
        response.put("collections", collections);
        return ResponseEntity.ok(response);
    }

}
