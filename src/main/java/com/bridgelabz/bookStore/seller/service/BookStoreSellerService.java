package com.bridgelabz.bookStore.seller.service;

import com.bridgelabz.bookStore.customer.dto.AddressDTO;
import com.bridgelabz.bookStore.customer.dto.ResetPassword;
import com.bridgelabz.bookStore.customer.dto.UserDTO;
import com.bridgelabz.bookStore.customer.modle.AddressDetail;
import com.bridgelabz.bookStore.customer.repository.IAddressRepository;
import com.bridgelabz.bookStore.customer.repository.ISelectedRepository;
import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.admin.repository.IBookRepository;
import com.bridgelabz.bookStore.admin.repository.ICartRepository;
import com.bridgelabz.bookStore.seller.model.AcceptedBook;
import com.bridgelabz.bookStore.seller.model.NewAddedBook;
import com.bridgelabz.bookStore.seller.model.RejectedBook;
import com.bridgelabz.bookStore.seller.repository.IAcceptedRepository;
import com.bridgelabz.bookStore.seller.repository.INewAddedBookRepository;
import com.bridgelabz.bookStore.seller.repository.IRejectedBookRepository;
import com.bridgelabz.bookStore.seller.repository.ISellerRepository;
import com.bridgelabz.bookStore.seller.model.Seller;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStoreSellerService implements IBookStoreSeller {

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    private IAddressRepository iAddressRepository;

    @Autowired
    private ISelectedRepository iSelectedRepository;

    @Autowired
    private ICartRepository iCartRepository;

    @Autowired
    private ISellerRepository iSellerRepository;

    @Autowired
    private INewAddedBookRepository iNewAddedBookRepository;

    @Autowired
    private IRejectedBookRepository iRejectedBookRepository;

    @Autowired
    private IAcceptedRepository iAcceptedRepository;

    @Autowired
    MailService mail;

    @Override
    public String registerUser(UserDTO userDTO) {
        if (iSellerRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new BookStoreException("Account with this email is already present");
        Seller customer = new Seller();
        customer.setFullName(userDTO.getFullName());
        customer.setEmail(userDTO.getEmail());
        customer.setMobileNo(userDTO.getMobileNo());
        customer.setPassword(userDTO.getPassword());
        Seller user = iSellerRepository.save(customer);
        mail.sendSellerMailWithTokenURL(userDTO.getEmail(), Token.getToken(user.getId()));
        return "Successfully Registered";
    }

    @Override
    public String verifyUser(String token) {
        Optional<Seller> customer = iSellerRepository.findById(Token.decodeJWT(token));
        if (customer.isPresent()){
            customer.get().setVerified(true);
            iSellerRepository.save(customer.get());
            return "Your emailId is verified";
        }
        throw new BookStoreException("Please Please re-register for verification");
    }

    @Override
    public String loginUser(UserDTO userDTO) {
        Optional<Seller> customer = iSellerRepository.findByEmail(userDTO.getEmail());
        if (customer.isPresent()){
            if (customer.get().getVerified()){
                if (customer.get().getPassword().equals(userDTO.getPassword())){
                    return Token.getToken(customer.get().getId());
                }
            }
            throw new BookStoreException("Please Verify your emailId at the provided Mail");
        }
        throw new BookStoreException("This email not registered");
    }

    @Override
    public String forgotPassword(UserDTO userDTO) {
        Optional<Seller> customer = iSellerRepository.findByEmail(userDTO.getEmail());
        mail.sendResetMessage(userDTO.getEmail(), Token.getToken(customer.get().getId()));
        return "Check your email for reset password link";
    }

    @Override
    public String resetPassword(ResetPassword resetPassword, String token) {
        Optional<Seller> customer = iSellerRepository.findById(Token.decodeJWT(token));
        if (resetPassword.getConformPassword().equals(resetPassword.getNewPassword())){
            if (customer.isPresent()){
                customer.get().setPassword(resetPassword.getNewPassword());
                iSellerRepository.save(customer.get());
                return "The password has been successfully reset";
            }
            throw new BookStoreException("Please follow the reset method again this link haas expired");
        }
        throw new BookStoreException("the new and confirm password are not matching");
    }

    @Override
    public Seller editUser(String userToken, UserDTO userDTO) {
        Optional<Seller> customer = iSellerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()){
            customer.get().setFullName(userDTO.getFullName());
            customer.get().setEmail(userDTO.getEmail());
            customer.get().setMobileNo(userDTO.getMobileNo());
            customer.get().setPassword(userDTO.getPassword());
            return iSellerRepository.save(customer.get());
        }
        throw new BookStoreException("Login session time out");
    }



    @Override
    public Seller addAddress(String userToken, AddressDTO addressDTO) {
        Optional<Seller> customer = iSellerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
            List<AddressDetail> addressDetails = customer.get().getAddressDetail();
            AddressDetail addressDetail  = new AddressDetail();
            addressDetail.setAddress(addressDTO.getAddress());
            addressDetail.setCity(addressDTO.getCity());
            addressDetail.setLandmark(addressDTO.getLandmark());
            addressDetail.setLocality(addressDTO.getLocality());
            addressDetail.setPinCode(addressDTO.getPinCode());
            addressDetail.setType(addressDTO.getType());
            AddressDetail address = iAddressRepository.save(addressDetail);
            addressDetails.add(address);
            customer.get().setAddressDetail(addressDetails);
            return iSellerRepository.save(customer.get());
        }
        throw new BookStoreException("Login session time out");
    }

    @Override
    public Seller editAddress(String userToken, AddressDTO addressDTO) {
        Optional<Seller> customer = iSellerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
            List<AddressDetail> addressDetails = customer.get().getAddressDetail();
            addressDetails.stream()
                    .filter(addressDetail -> addressDetail.getId().equals(addressDTO.getId()))
                    .forEach(addressDetail -> {
                        addressDetail.setAddress(addressDTO.getAddress());
                        addressDetail.setCity(addressDTO.getCity());
                        addressDetail.setLandmark(addressDTO.getLandmark());
                        addressDetail.setLocality(addressDTO.getLocality());
                        addressDetail.setPinCode(addressDTO.getPinCode());
                        addressDetail.setType(addressDTO.getType());
                    });
            customer.get().setAddressDetail(addressDetails);
            return iSellerRepository.save(customer.get());
        }
        throw new BookStoreException("Login session time out");
    }

    @Override
    public String sendBookTOAdmin(String userToken, BookDTO bookDTO) {
        Optional<Seller> seller = iSellerRepository.findById(Token.decodeJWT(userToken));
        NewAddedBook newAddedBook = new NewAddedBook();
        newAddedBook.setBook(bookDTO.getBook());
        newAddedBook.setNoOfBookInStock(bookDTO.getUserSelectedQuantity());
        seller.get().getNewAddedBooks().add(iNewAddedBookRepository.save(newAddedBook));
        iSellerRepository.save(seller.get());
        mail.sendSimpleMessage("nnalin04@gmail.com", "Book Detail for selling Verification",
                "the following sre the book details \n" + bookDTO.getBook());
        return "email with book detail send";
    }

    @Override
    public String rejectedBook(String userToken, BookDTO bookDTO) {
        Optional<Seller> seller = iSellerRepository.findById(Token.decodeJWT(userToken));
        RejectedBook rejectedBook = new RejectedBook();
        rejectedBook.setBook(bookDTO.getBook());
        rejectedBook.setNoOfBookInStock(bookDTO.getUserSelectedQuantity());
        rejectedBook.setReasonForRejection(bookDTO.getReason());
        seller.get().getRejectedBooks().add(iRejectedBookRepository.save(rejectedBook));
        List<NewAddedBook> newAddedBooks = seller.get().getNewAddedBooks();
        newAddedBooks.forEach(newBook -> {
            if (newBook.getBook().equals(bookDTO.getBook())){
                newAddedBooks.remove(newBook);
            }
        });
        iSellerRepository.save(seller.get());
        return "Book was rejected by admin for sail";
    }

    @Override
    public String acceptedBook(String userToken, BookDTO bookDTO) {
        Optional<Seller> seller = iSellerRepository.findById(Token.decodeJWT(userToken));
        AcceptedBook acceptedBook = new AcceptedBook();
        acceptedBook.setBook(bookDTO.getBook());
        acceptedBook.setNoOfBookInStock(bookDTO.getUserSelectedQuantity());
        seller.get().getAcceptedBooks().add(iAcceptedRepository.save(acceptedBook));
        List<NewAddedBook> newAddedBooks = seller.get().getNewAddedBooks();
        newAddedBooks.forEach(newBook -> {
            if (newBook.getBook().equals(bookDTO.getBook())){
                newAddedBooks.remove(newBook);
            }
        });
        iBookRepository.save(bookDTO.getBook());
        iSellerRepository.save(seller.get());
        return "Book was accepted to be sold by admin";
    }
}
