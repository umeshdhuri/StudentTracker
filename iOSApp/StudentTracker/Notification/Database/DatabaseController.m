//
//  DatabaseController.m
//  Mishu
//
//  Created by AppKnetics on 10/06/14.
//  Copyright (c) 2014 Needy. All rights reserved.
//

#import "DatabaseController.h"
#import "Notifications.h"

@implementation DatabaseController

#pragma mark -
#pragma mark NSFatchRequest with entity name

#pragma dlist
+ (NSFetchRequest *)fetchRequestWithEntityName:(NSString *)entityName {
    return [[NSFetchRequest alloc] initWithEntityName:entityName];
}


+ (BOOL)insertNotification:(NSDictionary *)userInfo {
    
    BOOL returnStr;
    NSError *error;
    NSManagedObjectContext *context = [appDelegate managedObjectContext];
    
    Notifications *notiObj = [NSEntityDescription insertNewObjectForEntityForName:@"Notifications" inManagedObjectContext:appDelegate.managedObjectContext];
   
    notiObj.userid = [userInfo objectForKey:@"uid"];
    notiObj.message = [userInfo objectForKey:@"alert"];
    notiObj.datetime = [userInfo objectForKey:@"posted_on"];
    NSDateFormatter *DateFormatter=[[NSDateFormatter alloc] init];
    [DateFormatter setDateFormat:@"MM-dd-yyyy hh:mm:ss"];
    NSDate *nowVal = [NSDate date];
    notiObj.dateVal = nowVal ;
    
    NSArray *notiData = [self getNotificationData];
    int countVal = 0 ;
    if([notiData count]) {
        countVal = [notiData count] + 1 ;
        notiObj.notiid = [NSString stringWithFormat:@"%d", countVal] ;
    }else{
        notiObj.notiid = @"1" ;
    }
    
    if (![context save:&error]) {
        returnStr = NO;
    }else{
        returnStr = YES;
    }
    
    
    return returnStr ;
}

+ (NSArray *)getNotificationData {
 
    NSArray *userNotificationData = [[NSArray alloc] init];
    
    NSFetchRequest *fetchRequest = [self fetchRequestWithEntityName:@"Notifications"];
    
    NSSortDescriptor *sortDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"dateVal" ascending:NO];
    [fetchRequest setSortDescriptors:[NSArray arrayWithObject:sortDescriptor]];
    
    NSError *error = nil;
    userNotificationData = [appDelegate.managedObjectContext executeFetchRequest:fetchRequest error:&error];
    
    return userNotificationData ;
    
}

#pragma mark -
#pragma mark check book count based on BookID

+ (BOOL)checkMessageCount:(int) status {
    
    //Check books are available in Books table
  //  NSPredicate *predicate;
    //predicate = [NSPredicate predicateWithFormat:@"delete_status = %d", status];
    NSFetchRequest *request = [self fetchRequestWithEntityName:@"Notifications"];
   // [request setPredicate:predicate];
    
    NSError *err;
    NSUInteger count = [appDelegate.managedObjectContext countForFetchRequest:request error:&err];
    
    if(count == 0) {
        return NO;
    }else{
        return YES;
    }
    
}

+(BOOL) deleteNotification:(NSString *) notiId {
    
    NSPredicate *predicate;
    predicate = [NSPredicate predicateWithFormat:@"notiid = %@", notiId];
    NSFetchRequest *fetchRequest = [self fetchRequestWithEntityName:@"Notifications"];
    [fetchRequest setPredicate:predicate];
    
    NSError *error;
    NSArray *items = [appDelegate.managedObjectContext executeFetchRequest:fetchRequest error:&error];
    
    for (NSManagedObject *managedObject in items) {
        [appDelegate.managedObjectContext deleteObject:managedObject];
        [appDelegate.managedObjectContext save:&error];
    }
    return YES ;
}

+(BOOL) deleteAllNotification {
    
    NSPredicate *predicate;
    //predicate = [NSPredicate predicateWithFormat:@"notiid = %@", notiId];
    NSFetchRequest *fetchRequest = [self fetchRequestWithEntityName:@"Notifications"];
   // [fetchRequest setPredicate:predicate];
    
    NSError *error;
    NSArray *items = [appDelegate.managedObjectContext executeFetchRequest:fetchRequest error:&error];
    
    for (NSManagedObject *managedObject in items) {
        [appDelegate.managedObjectContext deleteObject:managedObject];
        [appDelegate.managedObjectContext save:&error];
    }
    return YES ;
}


@end
