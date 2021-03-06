import {Component, OnInit} from '@angular/core';
import {ListedRecipe} from '../../model/listed-recipe.model';
import {RecipeService} from '../../service/recipe/recipe.service';
import {Router} from "@angular/router";
import {Ingredient} from "../../model/ingredient.model";
import {IngredientService} from "../../service/ingredient/ingredient.service";
import {WarningResponse} from "../../model/warning-response.model";
import {WarningService} from "../../service/warning/warning.service";

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {
    warnings: WarningResponse[];

    recipes: ListedRecipe[];
    picture: ''
    searchInput: '' = ''
    doFilterScore = false
    scoreFilterNum = null
    scoreFilter = []
    filterIngredients: Ingredient[] = []
    ingredientOptions: any[] = []

    constructor(
        private recipeService: RecipeService,
        private ingredientService: IngredientService,
        private warningService: WarningService,
        private router: Router,
    ) {
    }

    ngOnInit() {
        this.recipeService.findAll().subscribe(data => {
            this.updateRecipes(data)
        });
        this.warningService.getWarningsForCurrentUser().subscribe(data => {
            this.warnings = data
        })
    }

    openRecipe(id: number) {
        this.router.navigate(['recipe/' + id])
    }


    findIngredient(i) {
        this.ingredientOptions = [];

        this.ingredientService.search(this.filterIngredients[i].name).subscribe(options => {
            this.ingredientOptions = options
        });

        this.searchInputChanged()
    }


    addIngredient() {
        this.filterIngredients.push({
            id: null,
            name: null,
        })
    }

    removeIngredient(i: number) {
        this.filterIngredients.splice(i, 1);
        this.searchInputChanged()
    }


    showScoreFilter(show: boolean) {
        this.doFilterScore = show
        if (show) {
            this.scoreFilter = [true, false, false, false, false]
            this.scoreFilterNum = 1
        } else {
            this.scoreFilter = []
            this.scoreFilterNum = null
        }
        this.searchInputChanged()
    }

    scoreSelected(i) {
        this.scoreFilterNum = i + 1;
        this.scoreFilter = []
        for (let j = 0; j < 5; j++)
            this.scoreFilter.push(j <= i);
        this.searchInputChanged()
    }


    searchInputChanged() {
        this.recipeService.search(this.searchInput, this.filterIngredients, this.scoreFilterNum).subscribe(data => {
            this.updateRecipes(data)
        });
    }

    private updateRecipes(recipes: ListedRecipe[]) {
        recipes.forEach(recipe => {
            recipe.reviewCount = recipe.reviews.length
            let total = 0
            recipe.reviews.forEach(review => total += review.score)
            recipe.averageScore = Math.floor(total / recipe.reviewCount)
            if (isNaN(recipe.averageScore)) recipe.averageScore = 0
        })
        this.recipes = recipes;
    }
}
