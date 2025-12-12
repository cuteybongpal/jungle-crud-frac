package com.example.frac.Repository;

import com.example.frac.Entity.FractalMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFractalMapRepository extends JpaRepository<FractalMap, Integer>
{
    public FractalMap findByXAndYAndScale(double x, double y, double scale);

    public boolean existsByXAndYAndScale(double x, double y, double scale);
}
