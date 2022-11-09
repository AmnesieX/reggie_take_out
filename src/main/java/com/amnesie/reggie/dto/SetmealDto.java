package com.amnesie.reggie.dto;

import com.amnesie.reggie.entity.Setmeal;
import com.amnesie.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
