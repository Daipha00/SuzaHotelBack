    package com.reservation.HotelManagement.Controller;

    import com.reservation.HotelManagement.Model.*;
    import com.reservation.HotelManagement.Repository.RoomImageRepo;
    import com.reservation.HotelManagement.Repository.RoomRepo;
    import com.reservation.HotelManagement.Repository.HotelRepo;
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
    @RequestMapping("/room")
    @CrossOrigin(origins = "http://localhost:3000")
    public class RoomController {

        @Autowired
        private RoomRepo roomRepo;

        @Autowired
        private RoomImageRepo roomImageRepo;
        @Autowired
        private HotelRepo hotelRepo;

        @PostMapping
        public ResponseEntity<RoomResponse> createRoom(
                @RequestParam("hotelId") Long hotelId,
                @RequestParam("roomType") String roomType,
                @RequestParam("pax") int pax,
                @RequestParam("description") String description,
                @RequestParam("images") MultipartFile[] images) {

            // Validate and fetch the associated hotel
            Hotel hotel = hotelRepo.findById(hotelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

            // Create and save the room
            Room room = new Room();
            room.setRoomType(roomType);
            room.setPax(pax);
            room.setDescription(description);
            room.setHotel(hotel);
            Room savedRoom = roomRepo.save(room);

            // Save images and associate them with the room
            List<Long> imageIds = new ArrayList<>();
            for (MultipartFile imageFile : images) {
                try {
                    Room_image image = new Room_image();
                    image.setImage(imageFile.getBytes());
                    image.setRoom(savedRoom);
                    Room_image savedImage = roomImageRepo.save(image);
                    imageIds.add(savedImage.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            RoomResponse roomResponse = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                    savedRoom.getPax(), savedRoom.getDescription(), imageIds);
            return new ResponseEntity<>(roomResponse, HttpStatus.CREATED);
        }


        @GetMapping
        public ResponseEntity<List<RoomResponse>> getAllRooms() {
            List<Room> rooms = roomRepo.findAll();
            List<RoomResponse> roomResponses = new ArrayList<>();

            for (Room room : rooms) {
                List<Long> imageIds = new ArrayList<>();
                List<Room_image> images = roomImageRepo.findByRoomId(room.getId());
                for (Room_image image : images) {
                    imageIds.add(image.getId());
                }
                RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(),
                        room.getPax(), room.getDescription(), imageIds);
                roomResponses.add(roomResponse);
            }

            return new ResponseEntity<>(roomResponses, HttpStatus.OK);
        }

        @GetMapping("/hotel/{hotelId}")
        public ResponseEntity<List<RoomResponse>> getRoomsByHotelId(@PathVariable Long hotelId) {
            // Fetch the hotel first (optional, for validation)
            Hotel hotel = hotelRepo.findById(hotelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

            // Fetch the rooms associated with the hotel
            List<Room> rooms = roomRepo.findByHotelId(hotelId);  // Assuming you have this method in the repo
            List<RoomResponse> roomResponses = new ArrayList<>();

            for (Room room : rooms) {
                List<Long> imageIds = new ArrayList<>();
                List<Room_image> images = roomImageRepo.findByRoomId(room.getId());
                for (Room_image image : images) {
                    imageIds.add(image.getId());
                }
                RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(),
                        room.getPax(), room.getDescription(), imageIds);
                roomResponses.add(roomResponse);
            }

            return new ResponseEntity<>(roomResponses, HttpStatus.OK);
        }


        @GetMapping("/{id}")
        public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
            Room room = roomRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

            // Fetch images associated with the room
            List<Long> imageIds = new ArrayList<>();
            List<Room_image> images = roomImageRepo.findByRoomId(room.getId());
            for (Room_image image : images) {
                imageIds.add(image.getId());
            }

            // Create a response object
            RoomResponse roomResponse = new RoomResponse(room.getId(), room.getRoomType(),
                    room.getPax(), room.getDescription(), imageIds);

            return new ResponseEntity<>(roomResponse, HttpStatus.OK);
        }

        @GetMapping("/{roomId}/image")
        public ResponseEntity<byte[]> getFirstImageForRoom(@PathVariable Long roomId) {
            // Get the hotel by ID (optional, for validation)
            Optional<Room> roomOptional = roomRepo.findById(roomId);
            if (!roomOptional.isPresent()) {
                return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
            }

            Room room = roomOptional.get();

            // Get the first image for the hotel
            List<Room_image> images = roomImageRepo.findByRoomIdOrderByIdAsc(roomId); // Assuming you have an ordering mechanism

            if (images.isEmpty()) {
                return ResponseEntity.notFound().build(); // Return 404 if no images found for the hotel
            }

            Room_image firstImage = images.get(0); // Get the first image
            return ResponseEntity.ok()
                    .contentType(getMediaType(firstImage)) // Dynamically set the content type
                    .body(firstImage.getImage()); // Return the image bytes
        }
        private MediaType getMediaType(Room_image image) {
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
        private String getFileExtension(Room_image image) {
            // You can implement logic here to check the file format of the image byte[] if needed
            // For simplicity, let's assume the file type is known or inferred from the byte[]
            return "jpg"; // Placeholder for actual logic, you can enhance this
        }

        @GetMapping("/{roomId}/images")
        public ResponseEntity<List<byte[]>> getAllImagesForRoom(@PathVariable Long roomId) {
            // Get the hotel by ID (optional, for validation)
            Optional<Room> roomOptional = roomRepo.findById(roomId);
            if (!roomOptional.isPresent()) {
                return ResponseEntity.notFound().build(); // Return 404 if the hotel is not found
            }

            Room room = roomOptional.get();

            // Get all images for the room
            List<Room_image> images = roomImageRepo.findByRoomIdOrderByIdAsc(roomId); // Fetch images by room ID

            if (images.isEmpty()) {
                return ResponseEntity.notFound().build(); // Return 404 if no images found for the room
            }

            // Extract image byte[] from the list
            List<byte[]> imageBytes = images.stream()
                    .map(Room_image::getImage) // Assuming Room_image has a `getImage` method returning byte[]
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(imageBytes); // Return the list of images as response
        }




        @PutMapping("/{id}")
        public ResponseEntity<RoomResponse> updateRoom(
                @PathVariable Long id,
                @RequestParam("roomType") String roomType,
                @RequestParam("pax") int pax,
                @RequestParam("description") String description,
                @RequestParam(value = "images", required = false) MultipartFile[] images) {

            Room room = roomRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

            // Update room details
            room.setRoomType(roomType);
            room.setPax(pax);
            room.setDescription(description);
            Room updatedRoom = roomRepo.save(room);

            // Process and save new images, if provided
            List<Long> imageIds = new ArrayList<>();
            if (images != null) {
                for (MultipartFile imageFile : images) {
                    try {
                        Room_image image = new Room_image();
                        image.setImage(imageFile.getBytes());
                        image.setRoom(updatedRoom);
                        Room_image savedImage = roomImageRepo.save(image);
                        imageIds.add(savedImage.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // Fetch existing image IDs if no new images are provided
                List<Room_image> existingImages = roomImageRepo.findByRoomId(id);
                for (Room_image image : existingImages) {
                    imageIds.add(image.getId());
                }
            }

            // Create a response object
            RoomResponse roomResponse = new RoomResponse(updatedRoom.getId(), updatedRoom.getRoomType(),
                    updatedRoom.getPax(), updatedRoom.getDescription(), imageIds);

            return new ResponseEntity<>(roomResponse, HttpStatus.OK);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
            Room room = roomRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

            // Delete associated images first
            List<Room_image> images = roomImageRepo.findByRoomId(id);
            roomImageRepo.deleteAll(images);

            // Delete the room
            roomRepo.delete(room);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
