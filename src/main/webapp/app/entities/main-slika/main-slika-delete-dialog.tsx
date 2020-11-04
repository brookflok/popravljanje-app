import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate, ICrudGetAction, ICrudDeleteAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IMainSlika } from 'app/shared/model/main-slika.model';
import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './main-slika.reducer';

export interface IMainSlikaDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MainSlikaDeleteDialog = (props: IMainSlikaDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/main-slika');
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.mainSlikaEntity.id);
  };

  const { mainSlikaEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="popravljanjeApp.mainSlika.delete.question">
        <Translate contentKey="popravljanjeApp.mainSlika.delete.question" interpolate={{ id: mainSlikaEntity.id }}>
          Are you sure you want to delete this MainSlika?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button id="jhi-confirm-delete-mainSlika" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ mainSlika }: IRootState) => ({
  mainSlikaEntity: mainSlika.entity,
  updateSuccess: mainSlika.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MainSlikaDeleteDialog);
