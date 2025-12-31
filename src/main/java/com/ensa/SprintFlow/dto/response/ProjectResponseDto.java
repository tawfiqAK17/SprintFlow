package com.ensa.SprintFlow.dto.response;

import com.ensa.SprintFlow.enums.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProjectResponseDto {
  private Long id;
  private String name;
  private String description;
  private LocalDateTime creationDate;
  private UserMetaDataResponseDto scrumMaster;
  private UserMetaDataResponseDto productOwner;
  private List<Member> members;
  private List<Map<String, String>> statistics;

  @Setter
  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Member {
    private UserMetaDataResponseDto user;
    private Role role;
  }
}
