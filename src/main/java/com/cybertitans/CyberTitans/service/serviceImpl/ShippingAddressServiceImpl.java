package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.dto.ShippingAddressDTO;
import com.cybertitans.CyberTitans.dto.UserAddressResponseDTO;
import com.cybertitans.CyberTitans.exception.Exception;
import com.cybertitans.CyberTitans.exception.ResourceNotFoundException;
import com.cybertitans.CyberTitans.model.ShippingAddress;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.ShippingAddressRepository;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.service.ShippingAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ShippingAddressServiceImpl(ShippingAddressRepository shippingAddressRepository, UserRepository userRepository, ModelMapper mapper) {
        this.shippingAddressRepository = shippingAddressRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ShippingAddressDTO saveAddress(ShippingAddressDTO shippingAddressDTO) {
        User user = getLoggedInUser();
        ShippingAddress addressToBeSaved = mapper.map(shippingAddressDTO, ShippingAddress.class);
        addressToBeSaved.setUser(user);
        if (shippingAddressDTO.isDefaultShippingAddress()){
          addressToBeSaved.setDefaultShippingAddress(true);
        }
        ShippingAddress savedAddress = shippingAddressRepository.save(addressToBeSaved);
        return mapper.map(savedAddress, ShippingAddressDTO.class);
    }

    @Override
    public UserAddressResponseDTO getAllUserAddress(int pageNo, int pageSize, String sortBy, String sortDir) {
        User user = getLoggedInUser();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<ShippingAddress> shippingAddress = shippingAddressRepository.findAllByUser(user, pageable);
        List<ShippingAddress> addressContent =shippingAddress.getContent();
        List<ShippingAddressDTO> content =shippingAddress.stream().map(address -> mapper.map(address, ShippingAddressDTO.class)).collect(Collectors.toList());

        UserAddressResponseDTO userAddressResponseDTO = new UserAddressResponseDTO();
        userAddressResponseDTO.setContent(content);
        userAddressResponseDTO.setPageNo(shippingAddress.getNumber());
        userAddressResponseDTO.setPageSize(shippingAddress.getSize());
        userAddressResponseDTO.setTotalElements(shippingAddress.getNumberOfElements());
        userAddressResponseDTO.setTotalPages(shippingAddress.getTotalPages());
        userAddressResponseDTO.setLast(shippingAddress.isLast());
        return userAddressResponseDTO;
    }

    @Override
    public ShippingAddressDTO updateAddress(Long addressId, ShippingAddressDTO shippingAddressDTO) {
        ShippingAddress address = shippingAddressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        address.setStreetAddress(shippingAddressDTO.getStreetAddress());
        address.setCity(shippingAddressDTO.getCity());
        address.setState(shippingAddressDTO.getState());
        address.setDefaultShippingAddress(shippingAddressDTO.isDefaultShippingAddress());
        address.setCountry(shippingAddressDTO.getCountry());
        return mapper.map(address, ShippingAddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        ShippingAddress address = shippingAddressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        shippingAddressRepository.delete(address);
        return "Address Deleted Successfully";
    }


    @Override
    public ShippingAddressDTO getUserDefaultAddress() {
        User user = getLoggedInUser();
        Optional<ShippingAddress> addresses = shippingAddressRepository.findFirstByUserAndIsDefaultShippingAddressTrue(user);
        return mapper.map(addresses, ShippingAddressDTO.class);
    }

    private User getLoggedInUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
