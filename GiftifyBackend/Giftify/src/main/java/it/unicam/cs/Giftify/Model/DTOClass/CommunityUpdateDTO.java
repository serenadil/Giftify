package it.unicam.cs.Giftify.Model.DTOClass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityUpdateDTO {
    private String communityName;
    private String note;
    private Double budget;
}
