//
//  DatabaseController.h
//  Mishu
//
//  Created by AppKnetics on 10/06/14.
//  Copyright (c) 2014 Needy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DatabaseController : NSObject

/**
 FetchRequestWithEntityName
 @param entityName pass EntityName
 @returns return NSFetchRequest object
 */
+ (NSFetchRequest *)fetchRequestWithEntityName:(NSString *)entityName ;

/**
 Insert User Information in UserInfo Table
 @returns result in BOOL value
 */
+ (BOOL)insertNotification:(NSDictionary *)userInfo ;

/**
 Get Book Information Data
 @param bookID Book ID
 @param GUID User GUID
 @returns book information array
 */
+ (NSArray *)getNotificationData ;

+(BOOL) deleteNotification:(NSString *) notiId ;

+ (BOOL)checkMessageCount:(int) status ;

+(BOOL) deleteAllNotification ;

@end
