import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ListedRecipe} from '../model/listed-recipe';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class ListedRecipeService {

  private readonly recipesUrl: string;

  constructor(private http: HttpClient) {
    this.recipesUrl = 'http://localhost:8080/recipe';
  }

  public findAll(): Observable<ListedRecipe[]> {
    return this.http.get<ListedRecipe[]>(this.recipesUrl);
  }
}
