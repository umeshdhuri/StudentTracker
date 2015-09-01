//
//  Notifications.h
//  StudentTracker
//
//  Created by Umesh Dhuri on 6/2/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Notifications : NSManagedObject

@property (nonatomic, retain) NSDate * dateVal;
@property (nonatomic, retain) NSString * message;
@property (nonatomic, retain) NSString * datetime;
@property (nonatomic, retain) NSString * userid;
@property (nonatomic, retain) NSString *notiid ;

@end
