//
//  NotificationsviewViewController.m
//  StudentTracker
//
//  Created by AppKnetics on 29/05/15.
//  Copyright (c) 2015 AppKnetics. All rights reserved.
//

#import "NotificationsviewViewController.h"
#import "NotificationTableViewCell.h"
#import "Notifications.h"

@interface NotificationsviewViewController ()


@property (strong, nonatomic) sliderDrawe1ViewController *rootNav;

@end

@implementation NotificationsviewViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.rootNav = (sliderDrawe1ViewController *)self.navigationController;
    [self.rootNav setCCKFNavDrawerDelegate:self];
    
    // Do any additional setup after loading the view from its nib.
}

-(void) viewWillAppear:(BOOL)animated {
    notificationData = [[NSArray alloc] init];
    notificationData = [DatabaseController getNotificationData] ;
    NSLog(@"notificationData === %@", notificationData) ;
    self.notifivatntbl.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    
    CGRect notifivatntblFrame = self.notifivatntbl.frame ;
    if(isPhone480) {
        notifivatntblFrame.size.height = 408 ;
        self.notifivatntbl.frame = notifivatntblFrame ;
    }
    
    if([notificationData count]) {
        [self.clearBtn setHidden:NO];
        [self.clearLbl setHidden:NO];
        [self.noMsgLbl setHidden:YES];
        [self.notifivatntbl setHidden:NO] ;
        [self.notifivatntbl reloadData] ;
    }else{
        [self.clearBtn setHidden:YES];
        [self.clearLbl setHidden:YES];
        [self.noMsgLbl setHidden:NO];
        [self.notifivatntbl setHidden:YES] ;
    }
    
   
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    
    return 1;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.

    return [notificationData count];
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CGSize maximumStatusMsgLabelSize ;
    maximumStatusMsgLabelSize = CGSizeMake(250,200);
    Notifications *notiData = [notificationData objectAtIndex:indexPath.row] ;
    CGFloat height = 0;
    CGSize expectedTaskTitleLabelSize = [notiData.message sizeWithFont:[UIFont fontWithName:@"Gotham-Book" size:17]  constrainedToSize:maximumStatusMsgLabelSize lineBreakMode:NSLineBreakByWordWrapping];
    height = expectedTaskTitleLabelSize.height + 10;
    height = height + 35 ;
    NSLog(@"height === %f", height) ;
    return height;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"service";
    NotificationTableViewCell *cell = (NotificationTableViewCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (!cell) {
        [tableView registerNib:[UINib nibWithNibName: @"NotificationTableViewCell" bundle:nil] forCellReuseIdentifier:cellIdentifier];
        cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    }
    
    Notifications *notiData = [notificationData objectAtIndex:indexPath.row] ;
    
    CGSize maximumStatusMsgLabelSize ;
    maximumStatusMsgLabelSize = CGSizeMake(250,200);
    CGRect msgFrame = cell.msgLbl.frame ;
    [cell.msgLbl setText:notiData.message] ;
    CGSize expectedMessageLabelSize = [cell.msgLbl.text sizeWithFont:cell.msgLbl.font constrainedToSize:maximumStatusMsgLabelSize lineBreakMode:cell.msgLbl.lineBreakMode];
    msgFrame.size.height = expectedMessageLabelSize.height + 10;
    cell.msgLbl.numberOfLines = 0 ;
    cell.msgLbl.frame = msgFrame ;
    
    CGRect dateFrame = cell.dateLbl.frame ;
    dateFrame.origin.y = cell.msgLbl.frame.origin.y + cell.msgLbl.frame.size.height ;
    cell.dateLbl.frame = dateFrame ;
    
    NSString *dateString = notiData.datetime;
    NSLog(@"dateString == %@", dateString) ;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *date = [dateFormatter dateFromString:dateString];
    
    // Convert date object into desired format
    [dateFormatter setDateFormat:@"dd MMM, yyyy hh:mm:ss"];
    NSString *newDateString = [dateFormatter stringFromDate:date];
    
    [cell.dateLbl setText:newDateString] ;
    
    cell.deleteBtn.tag = indexPath.row ;
    [cell.deleteBtn addTarget:self action:@selector(deleteNotificationMsg:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.backgroundColor=[UIColor clearColor];
    //cell.servicelbl.text=[servicearr objectAtIndex:indexPath.row];
    tableView.separatorColor=[UIColor grayColor];
    tableView.backgroundColor=[UIColor colorWithRed:235/255.0 green:235/255.0 blue:235/255.0 alpha:1.0];
    cell.separatorInset=UIEdgeInsetsMake(-20,0,0,0);
    tableView.contentInset=UIEdgeInsetsMake(-0, 0, 0, 0);
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

-(void)deleteNotificationMsg:(id) sender {
    UIButton *btn = (UIButton *) sender ;
    Notifications *notiData = [notificationData objectAtIndex:btn.tag] ;
    [DatabaseController deleteNotification:notiData.notiid] ;
    
    notificationData = [DatabaseController getNotificationData] ;
    NSLog(@"notificationData === %@", notificationData) ;
    
    if([notificationData count]) {
        [self.noMsgLbl setHidden:YES];
        [self.notifivatntbl setHidden:NO] ;
        [self.notifivatntbl reloadData] ;
    }else{
        [self.noMsgLbl setHidden:NO];
        [self.notifivatntbl setHidden:YES] ;
    }
}

-(IBAction)deletAllNotification:(id)sender {
    
    UIAlertView *deleteAlert = [[UIAlertView alloc] initWithTitle:AppName message:@"Are you sure you want to delete all messages?" delegate:self cancelButtonTitle:@"Yes" otherButtonTitles:@"No", nil];
    [deleteAlert show];
    
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    if(buttonIndex == 0) {
        [DatabaseController deleteAllNotification] ;
        [self.noMsgLbl setHidden:NO];
        [self.notifivatntbl setHidden:YES] ;
        
        [self.clearBtn setHidden:YES];
        [self.clearLbl setHidden:YES];
    }
}

- (IBAction)drawrclicked:(id)sender {
    [self.rootNav drawerToggle];

}
@end
