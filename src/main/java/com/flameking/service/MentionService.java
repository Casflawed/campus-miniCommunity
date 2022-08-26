package com.flameking.service;

import com.flameking.entity.Mention;
import com.flameking.entity.Post;

import java.util.List;
import java.util.Set;

public interface MentionService {
    void addMention(Post post, Set<String> newSet);

    List<Mention> findMentionsByUid(Integer id);

    Mention findMentionById(Integer id);

    boolean deleteMention(Mention mention);


}
