package com.aghs.mobilevaccination.data.repository.location;

import com.aghs.mobilevaccination.data.model.location.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, String> {
}
