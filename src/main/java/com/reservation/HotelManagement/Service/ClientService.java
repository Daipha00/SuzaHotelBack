package com.reservation.HotelManagement.Service;

import com.reservation.HotelManagement.Model.Client;
import com.reservation.HotelManagement.Model.Reservation;
import com.reservation.HotelManagement.Repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    public List<Reservation> getReservationByClients(Long id){
        return clientRepo.getReservationByClient(id);
    }

//    public List<ReservationW> getAllReservationsWithClients() {
//        List<Reservation> reservations = reservationRepo.findAll();
//        List<ReservationWithClient> result = new ArrayList<>();
//
//        for (Reservation reservation : reservations) {
//            Client client = clientRepo.findById(reservation.getClient().getId()).orElseThrow();
//            ReservationWithClient reservationWithClient = new ReservationWithClient();
//            reservationWithClient.setReservation(reservation);
//            reservationWithClient.setClient(client);
//            result.add(reservationWithClient);
//        }
//
//        return result;
//    }
}
