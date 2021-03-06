package nl.hr.recipefinder.repository;

import nl.hr.recipefinder.model.entity.Report;
import nl.hr.recipefinder.model.entity.ReportKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, ReportKey> {
  List<Report> findAllByReportedUserId(Long userId);
}

