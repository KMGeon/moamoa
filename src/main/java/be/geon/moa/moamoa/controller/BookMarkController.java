package be.geon.moa.moamoa.controller;

import be.geon.moa.moamoa.ApiResponse;
import be.geon.moa.moamoa.config.log.Logging;
import be.geon.moa.moamoa.controller.dto.BookMarkResponse;
import be.geon.moa.moamoa.controller.dto.CreateBmarkReqest;
import be.geon.moa.moamoa.domain.Bookmark;
import be.geon.moa.moamoa.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @GetMapping
    @Logging(action = "POST", includeResponse = true)
    public ApiResponse<List<BookMarkResponse>> getAllBookmarks() {
        List<BookMarkResponse> rtn = bookMarkService.getAllBookmarks();
        return ApiResponse.success(rtn);
    }

    @PostMapping
    @Logging(action = "POST", includeResponse = true)
    public ApiResponse<Integer> createBookmark(
            @RequestBody CreateBmarkReqest bookmarkReqest
    ) {
        Bookmark rtn = bookMarkService.createBookmark(bookmarkReqest.url(), bookmarkReqest.category());
        return ApiResponse.maskToInteger(rtn);
    }
}
