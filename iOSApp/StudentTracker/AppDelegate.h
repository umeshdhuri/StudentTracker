//
//  AppDelegate.h
//  StudentTracker
//
//  Created by AppKnetics on 21/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
@interface AppDelegate : UIResponder <UIApplicationDelegate> {
    int currentHitCountAppDelegate ;
}

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, strong) NSString *deviceToken ;

@property (strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
@end
