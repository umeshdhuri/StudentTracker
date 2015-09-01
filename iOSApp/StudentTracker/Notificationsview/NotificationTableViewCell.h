//
//  NotificationTableViewCell.h
//  StudentTracker
//
//  Created by AppKnetics on 29/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NotificationTableViewCell : UITableViewCell

@property (nonatomic, weak) IBOutlet UILabel *msgLbl;
@property (nonatomic, weak) IBOutlet UILabel *dateLbl;
@property (nonatomic, weak) IBOutlet UIButton *deleteBtn;
@end
