package it.unicam.cs.Giftify.Model.Util.DTOClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityCreateDTO {
    private String communityName;
    private String communityNote;
    private double budget;
    private LocalDate deadline;
    private String userCommunityName;
}
