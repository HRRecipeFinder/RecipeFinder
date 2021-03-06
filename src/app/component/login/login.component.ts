import {Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../service/auth/auth.service";

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent {

  loading: any;

  credentials = {username: '', password: ''};

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router) {
  }

  login() {
    this.authService.authenticate(this.credentials, () => {
      if (this.authService.authenticated) {
        const previousRoute = this.route.snapshot.paramMap.get('previous');
        if (previousRoute){
          this.router.navigate([previousRoute]);
        }
        else{
          this.router.navigate(['']);
        }
      }
    });
  }

}
