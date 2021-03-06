import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {AdminService} from "../../service/admin/admin.service";
import {AdminIngredient} from "../../model/admin-ingredient.model";
import {Report} from "../../model/report.model";
import {Recipe} from "../../model/recipe.model";
import {User} from "../../model/user.model";
import {ReportService} from "../../service/report/report.service";
import {WarningResponse} from "../../model/warning-response.model";
import {WarningService} from "../../service/warning/warning.service";

@Component({
  selector: 'admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})

export class AdminComponent implements OnInit {
  loading: any;
  pendingIngredients: AdminIngredient[] = []
  rejectedIngredients: AdminIngredient[] = []
  reports: Report[] = []
  warnings: WarningResponse[] = []

  constructor(
    private router: Router,
    private adminService: AdminService,
    private reportService: ReportService,
    private warningService: WarningService,
  ) {
  }


  banReportedUser(reportedUser: User, i) {
    this.adminService.banUser(reportedUser).subscribe(result => {
      if (result)
        this.reports.splice(i, 1);
    });
  }


  banWarnedUser(warnedUser: User, i) {
    this.adminService.banUser(warnedUser).subscribe(result => {
      if (result)
        this.warnings.splice(i, 1);
    });
  }


  warnReportedUser(reportedUser: User, i) {
    this.router.navigate(['warn-user/' + reportedUser.id])
  }


  viewRecipe(recipe: Recipe, i) {
    this.router.navigate(['recipe/' + recipe.id])
  }


  acceptRejectedIngredient(ingredient: AdminIngredient, i) {
    this.adminService.acceptIngredient(ingredient).subscribe(ingredient => {
      if (ingredient)
        this.rejectedIngredients.splice(i, 1);
    });
  }


  acceptPendingIngredient(ingredient: AdminIngredient, i) {
    this.adminService.acceptIngredient(ingredient).subscribe(it => {
      if (it)
        this.pendingIngredients.splice(i, 1);
    });
  }

  rejectPendingIngedient(ingredient: AdminIngredient, i) {
    this.adminService.rejectIngredient(ingredient).subscribe(it => {
      if (it) {
        this.pendingIngredients.splice(i, 1);
        this.rejectedIngredients.push(it)
      }
    });
  }

  ngOnInit() {
    this.adminService.getPendingIngredients().subscribe(data => {
      this.pendingIngredients = data;
    });
    this.adminService.getRefusedIngredients().subscribe(data => {
      this.rejectedIngredients = data;
    });
    this.reportService.getReports().subscribe(data => {
      this.reports = data;
    })
    this.warningService.getWarnings().subscribe(data => {
      this.warnings = data;
    })
  }
}
