//
//  NotificationsviewViewController.h
//  StudentTracker
//
//  Created by AppKnetics on 29/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DrawerViewController.h"


@interface NotificationsviewViewController : UIViewController<UITableViewDataSource,UITableViewDelegate,CCKFNavDrawerDelegate> {
    NSArray *notificationData ;
}

@property (strong, nonatomic) IBOutlet UITableView *notifivatntbl;
@property (nonatomic, weak) IBOutlet UILabel *noMsgLbl ;
@property (nonatomic, weak) IBOutlet UIButton *clearBtn ;
@property (nonatomic, weak) IBOutlet UILabel *clearLbl ;

- (IBAction)drawrclicked:(id)sender;
+(BOOL) deleteAllNotification ;


@end
