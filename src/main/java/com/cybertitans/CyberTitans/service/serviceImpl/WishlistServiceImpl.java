package com.cybertitans.CyberTitans.service.serviceImpl;

import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.model.Wishlist;
import com.cybertitans.CyberTitans.repository.UserRepository;
import com.cybertitans.CyberTitans.repository.WishlistRepository;
import com.cybertitans.CyberTitans.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private WishlistRepository wishlistRepository;
    private UserRepository userRepository;

    public WishlistServiceImpl(WishlistRepository wishlistService,UserRepository userRepository) {
        this.wishlistRepository = wishlistService;
        this.userRepository = userRepository;
    }

    @Override
    public Wishlist createWishlist(Wishlist wishlist) {
        User user = getLoggedInUser();
        if(wishlistRepository.existsById(wishlist.getWishlistId())){
            // Wishlist Already exists
        } else{
            wishlist.setUser(user);
            wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

    @Override
    public List<Wishlist> getAllWishlists(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Wishlist> wishlistPage = wishlistRepository.findAll(pageable);

        List<Wishlist> wishlistList = wishlistPage.getContent();
        List<Wishlist> content = wishlistList.stream().collect(Collectors.toList());

        return content;
    }

    @Override
    public Wishlist getWishlistById(Long id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow();
        return wishlist;
    }

    @Override
    public Wishlist updateWishlist(Long id, Wishlist wishlist) {
        Wishlist newWishlist = wishlistRepository.findById(id).orElseThrow();

        newWishlist.setWishlistId(wishlist.getWishlistId());
        newWishlist.setUser(wishlist.getUser());
        newWishlist.setProduct(wishlist.getProduct());

        Wishlist editedWishlist = wishlistRepository.save(newWishlist);
        return editedWishlist;
    }

    @Override
    public Wishlist deleteWishlist(Long id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow();
        wishlistRepository.deleteById(id);
        return  wishlist;
    }

    private User getLoggedInUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(name, name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
