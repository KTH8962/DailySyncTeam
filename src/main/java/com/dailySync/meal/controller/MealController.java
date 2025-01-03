package com.dailySync.meal.controller;

import com.dailySync.common.ApiResponse;
import com.dailySync.meal.dto.*;
import com.dailySync.meal.entities.Meal;
import com.dailySync.meal.service.MealService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("api/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    /**
     * 로그인 한 유저가 연도 year 월 month 변수에 담아 넘겨주면 해당 월의 mealList 를 전달
     */
    @Operation
        (
            summary = "로그인한 유저가 선택한 달의 리스트 불러오기(초기 오늘의 월) - 일자별로 전체 리스트",
            description = "year는 연도 month는 월 입력한다. week값은 주로 보여주는것을 처리하기위해 추가되었음."
        )
    @GetMapping ("mealList/{year}/{month}")
    public ResponseEntity<ApiResponse<MealListResDto>> getMealList(
            @PathVariable int year,
            @PathVariable int month
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success(mealService.getUserMealList(userId, year, month));
    }

    /**
     * 로그인 한 유저가 연도 year 월 month 변수에 담아 넘겨주면 해당 월의 mealList 를 전달
     */
    @Operation
        (
            summary = "로그인한 유저가 선택한 달의 리스트 불러오기(초기 오늘의 월) - 일자별로 개수 카운팅",
            description = "year는 연도 month는 월 입력한다. week값은 주로 보여주는것을 처리하기위해 추가되었음."
        )
    @GetMapping ("mealDayList/{year}/{month}")
    public ResponseEntity<ApiResponse<MealDayResDto>> getDayMealList(
            @PathVariable int year,
            @PathVariable int month
    ) {
        Long userId = getUserId();
        return ApiResponse.success(mealService.getUserDayMealList(userId, year, month));
    }

    /**
     * 식단 추가 할 때 추천 리스트 recommandList 전달(관리자가 적용한 목록)
     */
    @Operation
        (
            summary = "추천 리스트",
            description = "관리자가 등록한 리스트 출력하므로 관리자로 지정된 아이디를 service단에 고정 id적용"
        )
    @GetMapping ("recommand")
    public ResponseEntity<ApiResponse<MealRecomResDto>> getRecommand() {
        return ApiResponse.success(mealService.getRecommandList());
    }

    /**
     * 식단 추가 할 때 즐겨찾기 리스트 favoriteList 전달
     */
    @Operation
        (
            summary = "즐겨찾기 리스트",
            description = "초기 등록시 즐겨찾기 버튼으로 선택한 리스트들 불러오기"
        )
    @GetMapping ("favorite")
    public ResponseEntity<ApiResponse<MealRecomResDto>> getFavorite() {
        Long userId = getUserId();
        return ApiResponse.success(mealService.getFavoriteList(userId));
    }

    /**
     * 로그인 유저리스트 전달
     */
    @Operation
        (
            summary = "식단 카테고리 리스트",
            description = "초기 등록시 즐겨찾기 버튼으로 선택한 리스트들 불러오기"
        )
    @GetMapping ("userInfo")
    public ResponseEntity<ApiResponse<List<MealCategoryResDto>>> getUser() {
        return ApiResponse.success(mealService.getCategoryList());
    }

    /**
     * 식단 리스트(데이터) 삽입
     */
    @Operation
        (
            summary = "식단 리스트(데이터) 삽입",
            description = "해당 일자 데이터 삽입할때 List에 객체를 담아와서 데이터 삽입"
        )
    @PostMapping ("add")
    public ResponseEntity<ApiResponse<Boolean>> postMealList(@RequestBody List<Meal> meals) throws Exception {
        Long userId = getUserId();
        return ApiResponse.success(mealService.insertMealList(userId, meals));
    }

    /**
     * 식단 데이터 수정
     */
    @Operation
        (
            summary = "식단 데이터 수정",
            description = "등록된 데이터를 수정한다."
        )
    @PatchMapping ("edit")
    public ResponseEntity<ApiResponse<Boolean>> patchMeals(@RequestBody MealReqDto mealReqDto) {
        return ApiResponse.success(mealService.updateMeal(mealReqDto));
    }

    /**
     * 식단목록 + 즐겨찾기 목록 삭제
     */
    @Operation
        (
            summary = "식단 목록 및 즐겨찾기 삭제",
            description = "식단이 가지고있는 고유 id를 사용하여 삭제한다."
        )
    @DeleteMapping ("delete/{id}")
    public ResponseEntity<ApiResponse<Boolean>> delectMeals(@PathVariable Long id) {
        return ApiResponse.success(mealService.deleteMeal(id));
    }

    private Long getUserId(){
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
