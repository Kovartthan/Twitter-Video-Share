/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ko.twitter.vplay.core;


import com.ko.twitter.vplay.core.api.DirectMessagesResources;
import com.ko.twitter.vplay.core.api.FavoritesResources;
import com.ko.twitter.vplay.core.api.FriendsFollowersResources;
import com.ko.twitter.vplay.core.api.HelpResources;
import com.ko.twitter.vplay.core.api.ListsResources;
import com.ko.twitter.vplay.core.api.PlacesGeoResources;
import com.ko.twitter.vplay.core.api.SavedSearchesResources;
import com.ko.twitter.vplay.core.api.SearchResource;
import com.ko.twitter.vplay.core.api.SpamReportingResource;
import com.ko.twitter.vplay.core.api.SuggestedUsersResources;
import com.ko.twitter.vplay.core.api.TimelinesResources;
import com.ko.twitter.vplay.core.api.TrendsResources;
import com.ko.twitter.vplay.core.api.TweetsResources;
import com.ko.twitter.vplay.core.api.UsersResources;
import com.ko.twitter.vplay.core.auth.OAuth2Support;
import com.ko.twitter.vplay.core.auth.OAuthSupport;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.2.0
 */
public interface Twitter extends java.io.Serializable,
        OAuthSupport,
        OAuth2Support,
    TwitterBase,
        TimelinesResources,
        TweetsResources,
        SearchResource,
        DirectMessagesResources,
        FriendsFollowersResources,
        UsersResources,
        SuggestedUsersResources,
        FavoritesResources,
        ListsResources,
        SavedSearchesResources,
        PlacesGeoResources,
        TrendsResources,
        SpamReportingResource,
        HelpResources {

    /**
    
     * @since Twitter4J 3.0.4
     */
    TimelinesResources timelines();

    /**
    
     * @since Twitter4J 3.0.4
     */
    TweetsResources tweets();

    /**
    
     * @since Twitter4J 3.0.4
     */
    SearchResource search();

    /**
    
     * @since Twitter4J 3.0.4
     */
    DirectMessagesResources directMessages();

    /**
    
     * @since Twitter4J 3.0.4
     */
    FriendsFollowersResources friendsFollowers();

    /**
    
     * @since Twitter4J 3.0.4
     */
    UsersResources users();

    /**
    
     * @since Twitter4J 3.0.4
     */
    SuggestedUsersResources suggestedUsers();

    /**
    
     * @since Twitter4J 3.0.4
     */
    FavoritesResources favorites();

    /**
    
     * @since Twitter4J 3.0.4
     */
    ListsResources list();

    /**
    
     * @since Twitter4J 3.0.4
     */
    SavedSearchesResources savedSearches();

    /**
    
     * @since Twitter4J 3.0.4
     */
    PlacesGeoResources placesGeo();

    /**
    
     * @since Twitter4J 3.0.4
     */
    TrendsResources trends();

    /**
     * @return
     * @since Twitter4J 3.0.4
     */
    SpamReportingResource spamReporting();

    /**
    
     * @since Twitter4J 3.0.4
     */
    HelpResources help();
}
