//package com.reservation.HotelManagement.Controller;
//import com.reservation.HotelManagement.Model.*;
//import com.reservation.HotelManagement.Repository.HotelRepo;
//import com.reservation.HotelManagement.Repository.ImageRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/hotel")
//@CrossOrigin(origins = "http://localhost:3000")
//public class HotelController {
//
//    @Autowired
//    private HotelRepo hotelRepository;
//
//    @Autowired
//    private ImageRepo imageRepository;
//
//    @PostMapping
//    public ResponseEntity<HotelResponse> createHotel(
//            @RequestParam("hotelName") String hotelName,
//            @RequestParam("location") String location,
//            @RequestParam("price") Double price,
//            @RequestParam("rating") Integer rating,
//            @RequestParam("images") MultipartFile[] images) {
//
//        // Create a new hotel entity
//        Hotel hotel = new Hotel();
//        hotel.setHotelName(hotelName);
//        hotel.setLocation(location);
//        hotel.setPrice(price);
//        hotel.setRating(rating);
//
//        // Save the hotel to the database first
//        Hotel savedHotel = hotelRepository.save(hotel);
//
//        // List to hold the saved image IDs
//        List<Long> imageIds = new ArrayList<>();
//
//        // Process the uploaded images and save them
//        for (MultipartFile imageFile : images) {
//            try {
//                // Create a new Image entity
//                Image image = new Image();
//                image.setImage(imageFile.getBytes()); // Store the image data
//
//                // Set the hotel reference to the saved hotel
//                image.setHotel(savedHotel);
//
//                // Save the image to the database
//                Image savedImage = imageRepository.save(image);
//                imageIds.add(savedImage.getId()); // Add the saved image ID to the list
//            } catch (Exception e) {
//                // Handle exceptions (e.g., log the error)
//                e.printStackTrace();
//            }
//        }
//
//        // Create a response object including the ID and image IDs
//        HotelResponse hotelResponse = new HotelResponse(savedHotel.getId(), savedHotel.getHotelName(), savedHotel.getLocation(), savedHotel.getPrice(), savedHotel.getRating(), imageIds);
//        return new ResponseEntity<>(hotelResponse, HttpStatus.CREATED);
//    }
//
//
//
//
//    @GetMapping
//    public ResponseEntity<List<HotelResponse>> getAllHotels() {
//        List<Hotel> hotels = hotelRepository.findAll();
//        List<HotelResponse> hotelResponses = new ArrayList<>();
//
//        for (Hotel hotel : hotels) {
//            List<Long> imageIds = new ArrayList<>();
//
//            // Fetch images for the current hotel
//            List<Image> images = imageRepository.findByHotelId(hotel.getId());
//
//            for (Image image : images) {
//                imageIds.add(image.getId());
//            }
//
//            HotelResponse hotelResponse = new HotelResponse(hotel.getId(), hotel.getHotelName(), hotel.getLocation(), hotel.getPrice(), hotel.getRating(), imageIds);
//            hotelResponses.add(hotelResponse);
//        }
//
//        return new ResponseEntity<>(hotelResponses, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
//        // Fetch the hotel by ID
//        Hotel hotel = hotelRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
//
//        // Fetch images for the hotel
//        List<Image> images = imageRepository.findByHotelId(hotel.getId());
//
//        // Collect image IDs
//        List<Long> imageIds = new ArrayList<>();
//        for (Image image : images) {
//            imageIds.add(image.getId());
//        }
//
//        // Create the response object
//        HotelResponse hotelResponse = new HotelResponse(hotel.getId(), hotel.getHotelName(), hotel.getLocation(), hotel.getPrice(), hotel.getRating(), imageIds);
//
//        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
//    }
//
//    @GetMapping("/{hotelId}/image")
//    public ResponseEntity<byte[]> getFirstImageForHotel(@PathVariable Long hotelId) {
//        // Get the hotel by ID (optional, for validation)
//        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
//        if (!hotelOptional.isPresent()) {
//            return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
//        }
//
//        Hotel hotel = hotelOptional.get();
//
//        // Get the first image for the hotel
//        List<Image> images = imageRepository.findByHotelIdOrderByIdAsc(hotelId); // Assuming you have an ordering mechanism
//
//        if (images.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Return 404 if no images found for the hotel
//        }
//
//        Image firstImage = images.get(0); // Get the first image
//        return ResponseEntity.ok()
//                .contentType(getMediaType(firstImage)) // Dynamically set the content type
//                .body(firstImage.getImage()); // Return the image bytes
//    }
//    private MediaType getMediaType(Image image) {
//        // Get the file extension or check the first few bytes of the image to determine the format
//        String fileExtension = getFileExtension(image);
//
//        switch (fileExtension) {
//            case "jpg":
//            case "jpeg":
//                return MediaType.IMAGE_JPEG;
//            case "png":
//                return MediaType.IMAGE_PNG;
//            case "gif":
//                return MediaType.IMAGE_GIF;
//            default:
//                return MediaType.APPLICATION_OCTET_STREAM; // Default for unknown image types
//        }
//    }
//
//    // Helper method to get file extension (basic example)
//    private String getFileExtension(Image image) {
//        // You can implement logic here to check the file format of the image byte[] if needed
//        // For simplicity, let's assume the file type is known or inferred from the byte[]
//        return "jpg"; // Placeholder for actual logic, you can enhance this
//    }
//
//    @GetMapping("/images/{imageId}")
//    public ResponseEntity<byte[]> getEachImageById(@PathVariable Long imageId) {
//        Optional<Image> image = imageRepository.findById(imageId);
//        if (image.isPresent()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG) // Adjust the content type if needed
//                    .body(image.get().getImage());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
////    @GetMapping("/{hotelId}/images")
////    public ResponseEntity<List<String>> getAllImagesForHotel(@PathVariable Long hotelId) {
////        // Check if the hotel exists
////        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
////        if (!hotelOptional.isPresent()) {
////            return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
////        }
////
////        // Fetch all images for the hotel
////        List<Image> images = imageRepository.findByHotelIdOrderByIdAsc(hotelId);
////        if (images.isEmpty()) {
////            return ResponseEntity.notFound().build(); // Return 404 if no images found
////        }
////
////        // Construct URLs for the images
////        List<String> imageUrls = images.stream()
////                .map(image -> String.format("http://localhost:9090/hotel/images/%d", image.getId())) // Replace with actual image endpoint
////                .collect(Collectors.toList());
////
////        return ResponseEntity.ok().body(imageUrls);
////    }
//
//    @GetMapping("/{hotelId}/images")
//    public ResponseEntity<List<Image>> getHotelImages(@PathVariable Long hotelId) {
//        // Find the hotel by ID
//        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
//
//        if (hotelOptional.isPresent()) {
//            Hotel hotel = hotelOptional.get();
//
//            // Retrieve all images associated with this hotel
//            List<Image> images = imageRepository.findByHotel(hotel);
//
//            return new ResponseEntity<>(images, HttpStatus.OK);
//        } else {
//            // Return a 404 if the hotel doesn't exist
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<HotelResponse> updateHotel(
//            @PathVariable Long id,
//            @RequestParam("hotelName") String hotelName,
//            @RequestParam("location") String location,
//            @RequestParam("price") Double price,
//            @RequestParam("rating") Integer rating,
//            @RequestParam(value = "images", required = false) MultipartFile[] images) {
//
//        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
//        if (hotelOptional.isPresent()) {
//            Hotel hotel = hotelOptional.get();
//            hotel.setHotelName(hotelName);
//            hotel.setLocation(location);
//            hotel.setPrice(price);
//            hotel.setRating(rating);
//
//            // Update the hotel in the database
//            Hotel updatedHotel = hotelRepository.save(hotel);
//
//            // Declare the imageIds list here
//            List<Long> imageIds = new ArrayList<>();
//
//            // If images are provided, process them
//            if (images != null) {
//                for (MultipartFile imageFile : images) {
//                    try {
//                        // Create a new Image entity
//                        Image image = new Image();
//                        image.setImage(imageFile.getBytes()); // Store the image data
//
//                        // Set the hotel reference to the updated hotel
//                        image.setHotel(updatedHotel);
//
//                        // Save the image to the database
//                        Image savedImage = imageRepository.save(image);
//                        imageIds.add(savedImage.getId()); // Add the saved image ID to the list
//                    } catch (Exception e) {
//                        // Handle exceptions (e.g., log the error)
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            // Create a response object including the updated hotel details
//            HotelResponse hotelResponse = new HotelResponse(updatedHotel.getId(), updatedHotel.getHotelName(), updatedHotel.getLocation(),
//                    updatedHotel.getPrice(), updatedHotel.getRating(),
//
//                    imageIds);
//            return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
//        Optional<Hotel> hotelOptional = hotelRepository.findById(id);
//        if (hotelOptional.isPresent()) {
//            hotelRepository.delete(hotelOptional.get());
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Successfully deleted
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Hotel not found
//        }
//    }
//}


package com.reservation.HotelManagement.Controller;

import com.reservation.HotelManagement.Model.*;
import com.reservation.HotelManagement.Repository.HotelRepo;
import com.reservation.HotelManagement.Repository.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotel")
@CrossOrigin(originPatterns = "*")
public class HotelController {

    @Autowired
    private HotelRepo hotelRepo;

    @Autowired
    private ImageRepo imageRepo;

    // Create new hotel with images
    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(
            @RequestParam("hotelName") String hotelName,
            @RequestParam("location") String location,
            @RequestParam("price") double price,
            @RequestParam("rating") int rating,
            @RequestParam("images") MultipartFile[] images) {

        // Create a new hotel entity
        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelName);
        hotel.setLocation(location);
        hotel.setPrice(price);
        hotel.setRating(rating);

        // Save the hotel to the database first
        Hotel savedHotel = hotelRepo.save(hotel);

        // List to hold the saved image IDs
        List<Long> imageIds = new ArrayList<>();

        // Process the uploaded images and save them
        for (MultipartFile imageFile : images) {
            try {
                // Create a new Image entity
                Image image = new Image();
                image.setImage(imageFile.getBytes()); // Store the image data

                // Set the hotel reference to the saved hotel
                image.setHotel(savedHotel);

                // Save the image to the database
                Image savedImage = imageRepo.save(image);
                imageIds.add(savedImage.getId()); // Add the saved image ID to the list
            } catch (Exception e) {
                // Handle exceptions (e.g., log the error)
                e.printStackTrace();
            }
        }

        // Create a response object including the ID and image IDs
        HotelResponse hotelResponse = new HotelResponse(savedHotel.getId(), savedHotel.getHotelName(),
                savedHotel.getLocation(), savedHotel.getPrice(), savedHotel.getRating(), imageIds);

        return new ResponseEntity<>(hotelResponse, HttpStatus.CREATED);
    }

    // Get all hotels
    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        List<Hotel> hotels = hotelRepo.findAll();
        List<HotelResponse> hotelResponses = hotels.stream().map(hotel -> {
            List<Long> imageIds = imageRepo.findByHotelId(hotel.getId())
                    .stream()
                    .map(Image::getId)
                    .collect(Collectors.toList());

            return new HotelResponse(
                    hotel.getId(),
                    hotel.getHotelName(),
                    hotel.getLocation(),
                    hotel.getPrice(),
                    hotel.getRating(),
                    imageIds
            );
        }).collect(Collectors.toList());

        return new ResponseEntity<>(hotelResponses, HttpStatus.OK);
    }

    // Get hotel by ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        List<Long> imageIds = imageRepo.findByHotelId(hotel.getId())
                .stream()
                .map(Image::getId)
                .collect(Collectors.toList());

        HotelResponse hotelResponse = new HotelResponse(
                hotel.getId(),
                hotel.getHotelName(),
                hotel.getLocation(),
                hotel.getPrice(),
                hotel.getRating(),
                imageIds
        );

        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }

    // Get first image for hotel
    @GetMapping("/{hotelId}/image")
    public ResponseEntity<byte[]> getFirstImageForHotel(@PathVariable Long hotelId) {
        // Get the hotel by ID (optional, for validation)
        Optional<Hotel> hotelOptional = hotelRepo.findById(hotelId);
        if (!hotelOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
        }

        Hotel hotel = hotelOptional.get();

        // Get the first image for the hotel
        List<Image> images = imageRepo.findByHotelIdOrderByIdAsc(hotelId);

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no images found for the hotel
        }

        Image firstImage = images.get(0); // Get the first image
        return ResponseEntity.ok()
                .contentType(getMediaType(firstImage)) // Dynamically set the content type
                .body(firstImage.getImage()); // Return the image bytes
    }

    private MediaType getMediaType(Image image) {
        // Get the file extension or check the first few bytes of the image to determine the format
        String fileExtension = getFileExtension(image);

        switch (fileExtension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // Default for unknown image types
        }
    }

    // Helper method to get file extension (basic example)
    private String getFileExtension(Image image) {
        // You can implement logic here to check the file format of the image byte[] if needed
        // For simplicity, let's assume the file type is known or inferred from the byte[]
        return "jpg"; // Placeholder for actual logic, you can enhance this
    }

    // Get all images for hotel
    @GetMapping("/{hotelId}/images")
    public ResponseEntity<List<byte[]>> getAllImagesForHotel(@PathVariable Long hotelId) {
        // Get the hotel by ID (optional, for validation)
        Optional<Hotel> hotelOptional = hotelRepo.findById(hotelId);
        if (!hotelOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
        }

        Hotel hotel = hotelOptional.get();

        // Get all images for the hotel
        List<Image> images = imageRepo.findByHotelIdOrderByIdAsc(hotelId); // Fetch images by hotel ID

        if (images.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no images found for the hotel
        }

        // Extract image byte[] from the list
        List<byte[]> imageBytes = images.stream()
                .map(Image::getImage) // Assuming Image entity has a `getImage` method that returns the image byte array
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(imageBytes); // Return the list of images as response
    }

    // Update hotel details
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long id,
            @RequestParam("hotelName") String hotelName,
            @RequestParam("location") String location,
            @RequestParam("price") double price,
            @RequestParam("rating") int rating,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        hotel.setHotelName(hotelName);
        hotel.setLocation(location);
        hotel.setPrice(price);
        hotel.setRating(rating);

        Hotel updatedHotel = hotelRepo.save(hotel);

        // If new images are provided, replace the old ones
        if (images != null && images.length > 0) {
            // Delete old images
            imageRepo.findByHotelId(id).forEach(imageRepo::delete);

            // Save new images
            for (MultipartFile imageFile : images) {
                try {
                    Image image = new Image();
                    image.setImage(imageFile.getBytes());
                    image.setHotel(updatedHotel);
                    imageRepo.save(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<Long> imageIds = imageRepo.findByHotelId(updatedHotel.getId())
                .stream()
                .map(Image::getId)
                .collect(Collectors.toList());

        HotelResponse hotelResponse = new HotelResponse(
                updatedHotel.getId(),
                updatedHotel.getHotelName(),
                updatedHotel.getLocation(),
                updatedHotel.getPrice(),
                updatedHotel.getRating(),
                imageIds
        );

        return new ResponseEntity<>(hotelResponse, HttpStatus.OK);
    }

    // Delete hotel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        // Delete images associated with the hotel
        imageRepo.findByHotelId(hotel.getId()).forEach(imageRepo::delete);

        // Delete the hotel
        hotelRepo.delete(hotel);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
